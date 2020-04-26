package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Hall;
import edu.uga.cinemaapp.model.Movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("hallRepository")
public interface HallRepository extends JpaRepository<Hall, Integer> {

    Hall findByName(String name);
    
    // Optional<Hall> findById(int id);

    Hall deleteById(int id);


    @Query(value = "SELECT * FROM HALL H WHERE H.STATUS LIKE 0",
    nativeQuery = true)
    List<Hall> findAvailable();
}