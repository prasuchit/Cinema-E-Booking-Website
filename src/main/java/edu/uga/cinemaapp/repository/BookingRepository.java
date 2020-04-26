package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("bookingRepository")
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Optional<Booking> findById(int id);

}