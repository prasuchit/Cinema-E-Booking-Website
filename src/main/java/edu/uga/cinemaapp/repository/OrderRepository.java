package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Ordersummary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Ordersummary, Integer> {

    // Optional<Ordersummary> findById(int id);

}