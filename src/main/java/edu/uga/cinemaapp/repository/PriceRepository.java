package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Price;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("priceRepository")
public interface PriceRepository extends JpaRepository<Price, Integer> {

    // Optional<Price> findById(int id);

}