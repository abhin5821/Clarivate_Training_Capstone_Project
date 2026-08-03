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

-- 1. Manager
INSERT IGNORE INTO managers(manager_id, name, phone, email, password)
VALUES(1, 'John Smith', '9999999999','johnsmith@gmail.com', '$2a$10$gUfMKEC7KJ7DOoMMW1vnCeWoXXhKU0MUoKYE9OIzNo0suenFw5xgy'); --password use 123 for login test


-- 2. Waiter (references manager_id = 1)
INSERT IGNORE INTO waiters (waiter_id, name, phone, email, manager_id,password)
VALUES (1, 'David Updated', '7777777777', 'davidupdated@gmail.com', 1,'$2a$10$YRDNq/DbV42aCvB4iBQjL.Tplf14OnYSzYYvCuyy.xFQWDiN1oaju'); --password waiter123

-- 3. Customer
INSERT IGNORE INTO customers (customer_id, name, phone, email,password)
VALUES (1, 'Priya verma', '9999999999', 'priyaverma@gmail.com','$2a$10$WxXdHBl.ooGJNLIXj5osju0CX8QpDHY6Bd1bZV6GZuOQ73tKVx0um'); --password priya123

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
    (1, '2026-07-21 17:43:21.518289', 'CONFIRMED', 1500.0, 1, 1),
    (2, '2026-07-22 20:50:15.239554', 'PENDING',   650.0,  1, 1);

-- 8. Payment (references order_id = 1)
INSERT IGNORE INTO payments (payment_id, amount, payment_method, payment_time, status, order_id)
VALUES (1, 1500.0, 'CASH', '2026-07-30 17:16:20.417083', 'PAID', 1);
