package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Showtime;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("showtimeRepository")
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {

    
    // Optional<Showtime> findById(int id);

    Showtime findByName(String name);

    Showtime findByDate(String date);

    Showtime findByTime(String time);

    @Query(value = "select * from showtime sh where sh.date like %:date% and sh.availability like '0'", nativeQuery = true)
    List<Showtime> findAllByDate(@Param("date") String date);

    @Query(value = "SELECT * FROM SHOWTIME S WHERE S.AVAILABILITY LIKE 0", nativeQuery = true)
    List<Showtime> findAvailable();

    @Query(value = "SELECT SHOWTIME_ID FROM MOVIE_SHOWTIME MS WHERE MS.MOVIE_ID = ?", nativeQuery = true)
    List<Showtime> findByMovie(int movie_id);

    @Query(value = "select distinct date from showtime sh where sh.availability like '0'", nativeQuery = true)
    List<String> findDistinctAvailDates();

    // Optional<Showtime> findByShowtime_id(int showtime_id);

    // Showtime deleteById(int id);

    // @Query(value = "SELECT * FROM SHOWTIME S WHERE S.NAME LIKE %:keyword%",
    // nativeQuery = true)
    // List<Showtime> searchInName(@Param("keyword") String keyword);

    // @Query(value = "SELECT * FROM SHOWTIME S WHERE S.AVAILABILITY = 1",
    // nativeQuery = true)
    // List<Showtime> findUnavailable();

    // @Query(value = "SELECT * FROM SHOWTIME S WHERE S.AVAILABILITY = 0",
    // nativeQuery = true)
    // List<Showtime> findAvailable();

}

// @Query(value = "SELECT * FROM movie M WHERE M.title LIKE %:title%",
// nativeQuery = true)
// List<Movie> findByTitle(@Param("title") String title);