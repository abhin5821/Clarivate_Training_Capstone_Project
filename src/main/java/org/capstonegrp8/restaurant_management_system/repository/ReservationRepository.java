package org.capstonegrp8.restaurant_management_system.repository;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}