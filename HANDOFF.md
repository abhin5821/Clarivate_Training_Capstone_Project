# Table Allocation — Handoff Summary

**Context:** Spring Boot + JPA (MySQL) restaurant system. We are incrementally
implementing a **queue-based auto table-allocation** algorithm, deliberately kept
simple using the **existing enums** for now. Bigger state-machine/concurrency work
is deferred (see below).

---

## Current states (unchanged, by design)

- `ReservationStatus`: `PENDING, CONFIRMED, CANCELLED`
- `TableStatus`: `AVAILABLE, RESERVED, OCCUPIED`

**Agreed mapping:**
- `PENDING`  = waiting in queue (no table yet)
- `CONFIRMED` = has a table (table is `RESERVED`)
- `OCCUPIED` = customer seated

---

## Done

1. **Rename** `Reservation.numberOfGuests` -> `partySize` (entity + service).
   - WARNING: API JSON request field is now `"partySize"` (was `"numberOfGuests"`).
2. **Validation block** added in `ReservationServiceImpl.createReservation()` via
   `validateReservation()`:
   - customer exists (looked up via `CustomerRepository`)
   - party size > 0
   - reservation time not null / not in the past
   - within operating hours (`restaurant.opening-hour=10`, `restaurant.closing-hour=22`
     in `application.properties`, injected with `@Value`)
   - party size <= `RestaurantTableRepository.findMaxCapacity()`
   - duplicate guard:
     `ReservationRepository.existsByCustomer_CustomerIdAndReservationDate(...)`
   - removed debug `System.out.println` statements.
3. Compiles clean via `.\mvnw.cmd compile`.

---

## Pending (priority order)

1. **Step 3 - Auto-allocation (NEXT UP):** stop making the client pick a table.
   Create reservation as `PENDING` with no table, then assign the best `AVAILABLE`
   table where `capacity >= partySize` using **minimum seat-waste**
   (`capacity - partySize`), tie-break by earliest reservation. On success ->
   table becomes `RESERVED`, reservation becomes `CONFIRMED`.
2. **Release + re-allocate:** on cancel / finish, set table back to `AVAILABLE`,
   then re-run allocation for the oldest `PENDING`.
3. **Seating & payment hooks:** waiter seats -> table `OCCUPIED`; payment confirmed
   -> do NOT auto-free table; cleaning done -> `AVAILABLE` + re-trigger allocation.
4. **Repository eligibility queries:** `findByStatus(AVAILABLE)`, oldest `PENDING`
   ordered by `reservationDate`.
5. **Tests:** validation failures, min-waste selection, no-table-available,
   release -> reallocate.

---

## Deferred (explicitly out of scope for now)

- Richer state machine (`WAITING / ALLOCATED / SEATED / COMPLETED / NO_SHOW`) and
  `TableStatus.ALLOCATED / CLEANING`.
- Extra fields: `queueSequence`, `checkedInAt`, `allocatedAt`, `seatedAt`.
- Concurrency safety (pessimistic lock on table + `@Version` optimistic checks)
  and starvation / anti-fairness logic.

---

## Key files

- `src/main/java/.../entity/Reservation.java`
- `src/main/java/.../service/impl/ReservationServiceImpl.java`
- `src/main/java/.../repository/ReservationRepository.java`
- `src/main/java/.../repository/RestaurantTableRepository.java`
- `src/main/resources/application.properties`

---

## Reference: target algorithm (full vision)

```
ON reservation submitted:
    validate customer details
    validate party size
    validate reservation time
    validate operating hours
    check maximum supported capacity
    prevent duplicate submission
    create reservation
    assign unique queue sequence
    set initial status
    IF eligible for seating: mark WAITING, trigger allocation
    ELSE: retain as CONFIRMED until allocation window or check-in

ON customer check-in:
    validate reservation; record checkedInAt; status WAITING; trigger allocation

ON table marked AVAILABLE:
    begin transaction; lock available table
    find eligible WAITING reservations (checked in, valid window, party <= capacity)
    IF starvation threshold exceeded: choose oldest overdue eligible
    ELSE: min seat waste; tie-break earliest waiting, queue seq, reservation id
    IF no candidate: keep AVAILABLE; commit; stop
    lock candidate; revalidate; assign table; mark ALLOCATED x2; timestamp; commit
    notify waiter and customer

ON waiter seats customer: table OCCUPIED; reservation SEATED; record seatedAt
ON payment confirmed:     reservation COMPLETED; do NOT release table automatically
ON waiter finishes cleaning: table AVAILABLE; clear assignment; trigger allocation
```
