package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Seat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("seatRepository")
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    
    // Optional<Seat> findById(int id);
    // List<Seat> saveAll(List<Seat> seats);

    Seat deleteById(int id);

}