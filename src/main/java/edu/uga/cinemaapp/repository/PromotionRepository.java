package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Promotion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("promotionRepository")
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    Promotion findByPromotag(String promotag);

    // Optional<Promotion> findById(int id);

}