package edu.uga.cinemaapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.repository.MovieRepository;

@Controller
// @RequestMapping("/movie")
public class MovieController {

    // @Autowired
    // private UserService userService;
    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/movielist")
    public ModelAndView MoviesList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/home/movielist");
        List<Movie> movies = movieRepository.findComingSoon();
        movies.addAll(movieRepository.findAvailable());
        mav.addObject("movies", movies);
        return mav;
    }

    @RequestMapping(value = { "/moviesingle/{id}" }, method = RequestMethod.GET)
    public ModelAndView SingleMoviePage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            try {
                mav.addObject("movie", movie.get());
                // ReviewVM rev = new ReviewVM() {
                //     {

                //         movie_id = movie.get().getId();
                //         text = "FUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUCK!!!!";

                //     }
                // };
                // mav.addObject("review", rev);
                mav.setViewName("/home/moviesingle");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
        }
        // mav.addAttribute("element", new Element());
        return mav;
    }

    @RequestMapping(value = { "/editmovie" }, method = RequestMethod.GET)
    public ModelAndView AddMoviePage() {
        return null;
    }

    @RequestMapping(value = { "/editmovie" }, method = RequestMethod.POST)
    public ModelAndView AddMovieAction(@Valid Movie movie) {
        return null;
    }

}