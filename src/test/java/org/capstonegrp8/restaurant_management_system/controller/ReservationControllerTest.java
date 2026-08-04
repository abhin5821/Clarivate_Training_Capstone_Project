package org.capstonegrp8.restaurant_management_system.controller;

import java.util.List;

import org.capstonegrp8.restaurant_management_system.entity.Reservation;
import org.capstonegrp8.restaurant_management_system.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ReservationControllerTest {

    @Test
    void getReservationByIdDelegatesToService() {
        ReservationService reservationService = Mockito.mock(ReservationService.class);
        Reservation reservation = new Reservation();
        reservation.setReservationId(42L);
        when(reservationService.getReservationById(42L)).thenReturn(reservation);

        ReservationController controller = new ReservationController(reservationService);

        ResponseEntity<Reservation> response = controller.getReservationById(42L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42L, response.getBody().getReservationId());
    }

    @Test
    void getReservationsByCustomerIdDelegatesToService() {
        ReservationService reservationService = Mockito.mock(ReservationService.class);
        Reservation reservation = new Reservation();
        reservation.setReservationId(7L);
        when(reservationService.getReservationsByCustomerId(3L)).thenReturn(List.of(reservation));

        ReservationController controller = new ReservationController(reservationService);

        ResponseEntity<List<Reservation>> response = controller.getReservationsByCustomerId(3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(7L, response.getBody().get(0).getReservationId());
    }
}
