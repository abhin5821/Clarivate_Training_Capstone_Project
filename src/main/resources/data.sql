-- ============================================================
--  Seed data for Restaurant Management System
--  Runs on every application startup.
--  INSERT IGNORE silently discards duplicate entries,
--  so re-running never causes constraint violations.
--
--  Insertion order respects all foreign-key constraints:
--    managers → waiters → customers → restaurant_tables
--    → menu_items → reservations → restaurant_orders → payments
-- ============================================================

-- ------------------------------------------------------------
--  Schema migration for the passwordless customer model.
--  Adjusts the EXISTING `customers` table in place (never drops
--  it), so the `reservations.customer_id` foreign key and all
--  existing customer rows/ids stay completely intact.
--  Idempotent: safe to run on every startup.
-- ------------------------------------------------------------

-- Drop the legacy `password` column if it still exists
SET @col_exists := (SELECT COUNT(*) FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'customers' AND column_name = 'password');
SET @sql := IF(@col_exists > 0, 'ALTER TABLE customers DROP COLUMN password', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Drop any unique index on `email` (name varies by Hibernate version)
SET @idx_name := (SELECT INDEX_NAME FROM information_schema.statistics
    WHERE table_schema = DATABASE() AND table_name = 'customers'
      AND column_name = 'email' AND non_unique = 0
    LIMIT 1);
SET @sql2 := IF(@idx_name IS NOT NULL, CONCAT('ALTER TABLE customers DROP INDEX `', @idx_name, '`'), 'SELECT 1');
PREPARE stmt2 FROM @sql2;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

-- Make `email` nullable (it used to be NOT NULL, unique)
SET @email_exists := (SELECT COUNT(*) FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'customers' AND column_name = 'email');
SET @sql3 := IF(@email_exists > 0, 'ALTER TABLE customers MODIFY COLUMN email VARCHAR(255) NULL', 'SELECT 1');
PREPARE stmt3 FROM @sql3;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;

-- Add `city` column if it isn't there yet (Hibernate usually adds it first,
-- this is just a safety net)
SET @city_exists := (SELECT COUNT(*) FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'customers' AND column_name = 'city');
SET @sql4 := IF(@city_exists = 0, 'ALTER TABLE customers ADD COLUMN city VARCHAR(255) NULL', 'SELECT 1');
PREPARE stmt4 FROM @sql4;
EXECUTE stmt4;
DEALLOCATE PREPARE stmt4;

-- ------------------------------------------------------------
--  OrderStatus is now a 2-state enum: IN_PROGRESS, COMPLETED.
--  The `status` column was originally created by Hibernate as a
--  native MySQL ENUM(...) matching the old 5-state Java enum, so
--  it must be widened to VARCHAR before any old values can be
--  remapped (otherwise MySQL truncates/rejects the new values).
--  Idempotent: safe to run on every startup.
-- ------------------------------------------------------------
ALTER TABLE restaurant_orders MODIFY COLUMN status VARCHAR(20) NOT NULL;
UPDATE restaurant_orders SET status = 'IN_PROGRESS' WHERE status IN ('PENDING', 'CANCELLED');
UPDATE restaurant_orders SET status = 'COMPLETED' WHERE status IN ('CONFIRMED', 'SHIPPED', 'DELIVERED');

-- 1. Manager
INSERT IGNORE INTO managers(manager_id, name, phone, email, password)
VALUES(1, 'John Smith', '9999999999','johnsmith@gmail.com', '$2a$10$gUfMKEC7KJ7DOoMMW1vnCeWoXXhKU0MUoKYE9OIzNo0suenFw5xgy'); --password use 123 for login test


-- 2. Waiter (references manager_id = 1)
INSERT IGNORE INTO waiters (waiter_id, name, phone, email, manager_id,password)
VALUES (1, 'David Updated', '7777777777', 'davidupdated@gmail.com', 1,'$2a$10$YRDNq/DbV42aCvB4iBQjL.Tplf14OnYSzYYvCuyy.xFQWDiN1oaju'); --password waiter123

-- 3. Customer (passwordless — phone is the identifier)
INSERT IGNORE INTO customers (customer_id, name, phone, city, email)
VALUES (1, 'Priya verma', '9999999999', 'Mumbai', 'priyaverma@gmail.com');

-- 4. Restaurant Table (waiter unassigned in source DB)
INSERT IGNORE INTO restaurant_tables (table_id, table_number, capacity, status, waiter_id)
VALUES (1, 2, 6, 'RESERVED', NULL);

-- 5. Menu Item (manager unassigned in source DB)
INSERT IGNORE INTO menu_items (item_id, name, category, price, available, manager_id)
VALUES (1, 'Mutton Dum Biryani', 'MAIN_COURSE', 300.0, 1, NULL);

-- 6. Reservation (references customer_id = 1, table_id = 1)
INSERT IGNORE INTO reservations
    (reservation_id, reservation_date, party_size, status, customer_id, table_id)
VALUES (1, '2026-07-31 20:00:00.000000', 5, 'CONFIRMED', 1, 1);

-- 7. Restaurant Orders (both reference reservation_id = 1, waiter_id = 1)
INSERT IGNORE INTO restaurant_orders (order_id, order_time, status, total_amount, reservation_id, waiter_id)
VALUES
    (1, '2026-07-21 17:43:21.518289', 'COMPLETED',   1500.0, 1, 1),
    (2, '2026-07-22 20:50:15.239554', 'IN_PROGRESS', 650.0,  1, 1);

-- 8. Payments (one per order, matching the new invariant that every order
-- has exactly one linked payment from the moment it's created)
INSERT IGNORE INTO payments (payment_id, amount, payment_method, payment_time, status, order_id)
VALUES
    (1, 1500.0, 'CASH', '2026-07-30 17:16:20.417083', 'PAID', 1),
    (2, 650.0,  NULL,   NULL,                          'PENDING', 2);
