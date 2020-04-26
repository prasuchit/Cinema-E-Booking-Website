package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("movieRepository")
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Movie findByName(String name);

    Movie findByGenre(String genre);

    // Optional<Movie> findById(int id);

    Movie deleteById(int id);

    @Query(value = "SELECT * FROM MOVIE M WHERE M.NAME LIKE %:keyword%", nativeQuery = true)
    List<Movie> searchInName(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM MOVIE M WHERE M.GENRE LIKE %:keyword%", nativeQuery = true)
    List<Movie> searchInCategoryList(@Param("keyword") int keyword);

    @Query(value = "SELECT * FROM MOVIE M WHERE M.STATUS = 1", nativeQuery = true)
    List<Movie> findComingSoon();

    @Query(value = "SELECT * FROM MOVIE M WHERE M.STATUS = 0", nativeQuery = true)
    List<Movie> findAvailable();

}

// @Query(value = "SELECT * FROM movie M WHERE M.title LIKE %:title%",
// nativeQuery = true)
// List<Movie> findByTitle(@Param("title") String title);