package edu.uga.cinemaapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.UserRepository;
import edu.uga.cinemaapp.service.UserService;
import edu.uga.cinemaapp.viewmodel.ResetPasswdVM;

@RestController
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/review")

public class ReviewController {

    @Autowired
    MovieRepository movieRepository;

    @PostMapping("/post_review/")
    public ModelAndView MovieReviewPage(@PathVariable int id) {

        ModelAndView mav = new ModelAndView();
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            try {
                mav.addObject("movie", movie);
                mav.addObject("write_rev", true);
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
        }
        mav.setViewName("home/moviesingle");
        return mav;
    }
}