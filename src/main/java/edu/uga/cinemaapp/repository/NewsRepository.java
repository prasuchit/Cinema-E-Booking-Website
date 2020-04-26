package edu.uga.cinemaapp.repository;


import edu.uga.cinemaapp.model.News;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("newsRepository")
public interface NewsRepository extends JpaRepository<News, Integer> {

    News findByTitle(String title);
    
    // Optional<News> findById(int id);

    News deleteById(int id);

}