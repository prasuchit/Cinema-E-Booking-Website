package edu.uga.cinemaapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uga.cinemaapp.model.Hall;
import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.Role;
import edu.uga.cinemaapp.model.Showtime;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.model.News;
import edu.uga.cinemaapp.model.Price;
import edu.uga.cinemaapp.model.Promotion;
import edu.uga.cinemaapp.other.HallMaps;
import edu.uga.cinemaapp.other.MovieMaps;
import edu.uga.cinemaapp.other.ShowtimeMaps;
import edu.uga.cinemaapp.repository.HallRepository;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.RoleRepository;
import edu.uga.cinemaapp.repository.ShowtimeRepository;
import edu.uga.cinemaapp.repository.UserRepository;
import edu.uga.cinemaapp.viewmodel.AddUserVM;
import edu.uga.cinemaapp.viewmodel.ShowtimeVM;
import edu.uga.cinemaapp.repository.NewsRepository;
import edu.uga.cinemaapp.repository.PriceRepository;
import edu.uga.cinemaapp.repository.PromotionRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    PriceRepository priceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ShowtimeRepository showtimeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    NewsRepository newsRepository;

    @GetMapping({ "/index", "/", "" })
    public ModelAndView Home() {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/index");
        // mav.addObject("movie", new Movie());
        return mav;
    }

    @GetMapping("/manage_movies")
    public ModelAndView ManageMovies() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_movies");
        return mav;
    }

    @GetMapping("/manage_users")
    public ModelAndView ManageUsers() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_users");
        return mav;
    }

    @GetMapping("/manage_showtimes")
    public ModelAndView ManageShowtime() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_showtimes");
        return mav;
    }

    @GetMapping("/manage_halls")
    public ModelAndView ManageHalls() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_halls");
        return mav;
    }

    @GetMapping("/manage_news")
    public ModelAndView ManageNews() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_news");
        return mav;
    }

    @GetMapping("/manage_reports")
    public ModelAndView ManageReports() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/manage_reports");
        return mav;
    }

    @RequestMapping(value = "/add_movie", method = RequestMethod.GET)
    public ModelAndView AddMoviePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("movie", new Movie());
        mav.addObject("MovieMaps", MovieMaps.class);

        mav.setViewName("/admin/add_movie");
        return mav;
    }

    // the @valid checks to see if all values are valid. i.e. the movie name is not
    // null, etc.
    @PostMapping("/add_movie")
    public ModelAndView AddMovieAction(@Valid Movie movie, BindingResult BindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/add_movie");
        mav.addObject("MovieMaps", MovieMaps.class);
        try {
            movieRepository.save(movie);
            mav.addObject("message", "Movie added successfully!");

        } catch (Exception e) {
            mav.addObject("message", "Check all your inputs and try again!");

        }

        mav.addObject("movie", movie);
        // if (BindingResult.hasErrors()) {
        // mav.addObject("message", "you fucked up");
        // }
        return mav;
    }

    @GetMapping("/delete_movie/{id}")
    public ModelAndView DeleteMovieAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/movie_list");
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            try {
                movieRepository.delete(movie.get());
                mav.addObject("message", "Movie deleted successfully!!");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());

            }

        } else {
            mav.addObject("message", "No movie with that ID in the database!!");
        }
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        return mav;
    }

    @GetMapping("/edit_movie/{id}")
    public ModelAndView EditMoviePage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_movie");

        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isPresent()) {
            mav.addObject("movie", movie);
        } else {
            mav.addObject("message", "No movie with that ID in the database!!");
        }
        mav.addObject("MovieMaps", MovieMaps.class);

        return mav;
    }

    @PostMapping("/edit_movie")
    public ModelAndView EditMovieAction(@Valid Movie movie, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_movie");

        Optional<Movie> origMovie = movieRepository.findById(movie.getId());
        if (origMovie.isPresent()) {
            try {
                origMovie.get().setYear(movie.getYear());
                origMovie.get().setTrailerUrl(movie.getTrailerUrl());
                origMovie.get().setSynopsis(movie.getSynopsis());
                origMovie.get().setStatus(movie.getStatus());
                origMovie.get().setRating(movie.getRating());
                origMovie.get().setProducer(movie.getProducer());
                origMovie.get().setName(movie.getName());
                origMovie.get().setGenre(movie.getGenre());
                origMovie.get().setDuration(movie.getDuration());
                origMovie.get().setDirector(movie.getDirector());
                origMovie.get().setCoverImage(movie.getCoverImage());
                origMovie.get().setCast(movie.getCast());
                movieRepository.save(origMovie.get());
                mav.addObject("message", "Movie edited successfully!");

            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
            mav.addObject("movie", origMovie.get());
        } else {
            mav.addObject("message", "No movie with that ID in the database!!");
        }
        mav.addObject("MovieMaps", MovieMaps.class);

        return mav;
    }

    @GetMapping("/movie_list")
    public ModelAndView MovieList(Model model) {
        ModelAndView mav = new ModelAndView();
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        mav.setViewName("/admin/movie_list");
        // System.out.println(MovieMaps.genreMap.get(1));

        return mav;
    }

    @GetMapping("/schedule_movie")
    public ModelAndView SceduleMoviePage() {
        ModelAndView mav = new ModelAndView();
        List<Movie> movies = movieRepository.findAll();
        List<String> showtimes = showtimeRepository.findDistinctAvailDates();

        mav.addObject("showtimes", showtimes);
        mav.addObject("movies", movies);
        mav.setViewName("/admin/schedule_movie");
        return mav;
    }

    @PostMapping("/schedule_movie")
    public ModelAndView SceduleMovieAction(int movie_id, int showtime_id) {
        ModelAndView mav = new ModelAndView();
        try {

            Optional<Movie> movie = movieRepository.findById(movie_id);
            Optional<Showtime> showtime = showtimeRepository.findById(showtime_id);

            showtime.get().setAvailability(1);
            showtime.get().setMovie(movie.get());
            showtimeRepository.save(showtime.get());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            mav.addObject("message", e.getMessage());
        }
        List<Movie> movies = movieRepository.findAll();
        List<String> showtimes = showtimeRepository.findDistinctAvailDates();

        mav.addObject("showtimes", showtimes);
        mav.addObject("movies", movies);
        mav.setViewName("/admin/schedule_movie");
        return mav;
    }

    @GetMapping("/add_promotions")
    public ModelAndView AddPromotionPage() {
        ModelAndView mav = new ModelAndView();
        // List<Promotion> promotions = promoRepository.findAll();
        mav.addObject("promotion", new Promotion());
        mav.setViewName("/admin/add_promotion");
        return mav;
    }

    @PostMapping("/add_promotions")
    public ModelAndView AddPromotionAction(@Valid Promotion promotion, BindingResult bindingresult) {
        ModelAndView mav = new ModelAndView();
        // List<Promotion> promotions = promoRepository.findAll();
        try {
            promotionRepository.save(promotion);
            mav.addObject("message", "Promo added successfully!");

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());

        }
        mav.addObject("promotion", promotion);
        mav.setViewName("/admin/add_promotion");
        return mav;
    }

    @GetMapping("/edit_promotions")
    public ModelAndView EditPromotionPage() {
        ModelAndView mav = new ModelAndView();
        List<Promotion> promotions = promotionRepository.findAll();
        mav.addObject("promotions", promotions);
        mav.setViewName("/admin/edit_promotion");
        return mav;
    }

    @GetMapping("/edit_promo_single/{id}")
    public ModelAndView EditPromotionAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        Optional<Promotion> promotion = promotionRepository.findById(id);
        mav.addObject("promotion", promotion);
        mav.setViewName("/admin/edit_promo_single");
        return mav;
    }

    @PostMapping("/edit_promo_single")
    public ModelAndView EditPromotionSubmit(@Valid Promotion promotion, BindingResult bindingresult) {
        ModelAndView mav = new ModelAndView();
        try {
            promotion.setPromotag(promotion.getPromotag());
            promotion.setDisc_perc(promotion.getDisc_perc());
            promotion.setStatus(promotion.getStatus());
            promotionRepository.save(promotion);
            mav.addObject("message", "Promotion edited successfully!");

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
        }
        mav.addObject("promotion", promotion);
        mav.setViewName("/admin/edit_promo_single");
        return mav;
    }

    @GetMapping("/edit_prices")
    public ModelAndView EditOnlineFeesPage() {
        ModelAndView mav = new ModelAndView();
        List<Price> price = priceRepository.findAll();
        mav.addObject("price", price.get(0));
        mav.setViewName("/admin/edit_online_fees");
        return mav;
    }

    @PostMapping("/edit_prices")
    public ModelAndView EditOnlineFeesAction(@Valid Price price, BindingResult bindingresult) {
        ModelAndView mav = new ModelAndView();
        try {
            price.setAdult(price.getAdult());
            price.setChild(price.getChild());
            price.setElderly(price.getElderly());
            priceRepository.save(price);
            mav.addObject("message", "Prices edited successfully!");

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
        }
        mav.addObject("price", price);
        mav.setViewName("/admin/edit_online_fees");
        return mav;
    }

    @GetMapping("/add_user")
    public ModelAndView AddUserPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("addUserVM", new AddUserVM());
        mav.setViewName("/admin/add_user");
        return mav;
    }

    @PostMapping("/add_user")
    public ModelAndView AddUserAction(AddUserVM user, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        try {
            Role role = roleRepository.findByRole(user.getRole());
            User theUser = new User();
            theUser.setRole(role);
            theUser.setLastName(user.getLastName());
            theUser.setPassword(user.getPassword());
            theUser.setEmail(user.getEmail());
            theUser.setName(user.getName());
            theUser.setActive(user.getActive());
            userRepository.save(theUser);
            mav.addObject("message", "User added successfully!");

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
        }
        mav.addObject("user", user);
        mav.setViewName("/admin/add_user");
        return mav;
    }

    // @GetMapping("/delete_user")
    // public ModelAndView DeleteUserPage() {
    // ModelAndView mav = new ModelAndView();
    // List<User> users = userRepository.findAll();
    // mav.addObject("users", users);
    // mav.setViewName("/admin/delete_user");
    // return mav;
    // }

    // @GetMapping("/delete_user/{id}")
    // public ModelAndView DeleteUserAction(@PathVariable int id, RedirectAttributes
    // redirectAttributes) {
    // ModelAndView mav = new ModelAndView();
    // mav.setViewName("/admin/delete_user");
    // Optional<User> user = userRepository.findById(id);
    // if (user.isPresent()) {
    // try {
    // userRepository.delete(user.get());
    // mav.addObject("message", "User deleted successfully!!");
    // } catch (Exception e) {
    // mav.addObject("message", e.getMessage());

    // }

    // } else {
    // mav.addObject("message", "No movie with that ID in the database!!");
    // }
    // List<User> users = userRepository.findAll();
    // mav.addObject("users", users);
    // return mav;
    // }

    @GetMapping("/promote_user")
    public ModelAndView PromoteUserPage() {
        ModelAndView mav = new ModelAndView();
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/promote_user");
        return mav;
    }

    @GetMapping("/promote_user/{id}")
    public ModelAndView PromoteUserAction(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        Optional<User> user = userRepository.findById(id);
        Role role = roleRepository.findByRole("ROLE_ADMIN");
        if (user.isPresent()) {
            user.get().setRole(role);
            mav.addObject("message", "User promoted successfully!!");
        } else {
            mav.addObject("message", "No user with that ID in the database!!");
        }
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/promote_user");
        return mav;
    }

    @GetMapping("/demote_user")
    public ModelAndView DemoteUserPage() {
        ModelAndView mav = new ModelAndView();
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/demote_user");
        return mav;
    }

    @GetMapping("/demote_user/{id}")
    public ModelAndView DemoteUserAction(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        Optional<User> user = userRepository.findById(id);
        Role role = roleRepository.findByRole("ROLE_USER");
        if (user.isPresent()) {
            user.get().setRole(role);
            mav.addObject("message", "User demoted successfully!!");
        } else {
            mav.addObject("message", "No user with that ID in the database!!");
        }
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/demote_user");
        return mav;
    }

    @GetMapping("/suspend_user")
    public ModelAndView SuspendUserPage() {
        ModelAndView mav = new ModelAndView();
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/suspend_user");
        return mav;
    }

    @GetMapping("/suspend_user/{id}")
    public ModelAndView SuspendUserAction(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setActive(0);
            mav.addObject("message", "User suspended successfully!!");
        } else {
            mav.addObject("message", "No user with that ID in the database!!");
        }
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/suspend_user");
        return mav;
    }

    @GetMapping("/unsuspend_user/{id}")
    public ModelAndView UnsuspendUserAction(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setActive(1);
            mav.addObject("message", "User suspended successfully!!");
        } else {
            mav.addObject("message", "No user with that ID in the database!!");
        }
        List<User> users = userRepository.findAll();
        mav.addObject("users", users);
        mav.setViewName("/admin/suspend_user");
        return mav;
    }

    @GetMapping("/delete_showtime")
    public ModelAndView DeleteShowtimePage() {
        ModelAndView mav = new ModelAndView();
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        mav.setViewName("/admin/delete_showtime");
        return mav;
    }

    @GetMapping("/add_hall")
    public ModelAndView AddHallPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("hall", new Hall());
        mav.addObject("hallMaps", HallMaps.class);
        mav.setViewName("/admin/add_hall");
        return mav;
    }

    @PostMapping("/add_hall")
    public ModelAndView AddHallAction(@Valid Hall hall, BindingResult BindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("hallMaps", HallMaps.class);
        try {
            hallRepository.save(hall);
            mav.addObject("message", "Hall added successfully!");

        } catch (Exception e) {
            mav.addObject("message", "Check all your inputs and try again!");

        }
        mav.setViewName("/admin/add_hall");
        return mav;
    }

    @GetMapping("/gen_report")
    public ModelAndView GenReportPage() {
        ModelAndView mav = new ModelAndView();
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        mav.setViewName("/admin/gen_report");
        return mav;
    }

    // Add Showtime

    @RequestMapping(value = "/add_showtime", method = RequestMethod.GET)
    public ModelAndView AddShowtimePage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("showtime", new Showtime());
        mav.addObject("showtimeMaps", ShowtimeMaps.class);
        List<Hall> halls = hallRepository.findAll();
        mav.addObject("halls", halls);
        mav.setViewName("/admin/add_showtime");
        // System.out.println(ShowtimeMaps.availabilityMap.get(1));
        return mav;
    }

    // the @valid checks to see if all values are valid. i.e. the showtime name is
    // not
    // null, etc.
    @PostMapping("/add_showtime")
    public ModelAndView AddShowtimeAction(@Valid Showtime showtime, BindingResult BindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/add_showtime");
        mav.addObject("showtimeMaps", ShowtimeMaps.class);
        try {
            Hall hall = hallRepository.findById(showtime.getHall_id()).get();
            showtime.setHall(hall);
            showtime.setAvailability(0);
            showtimeRepository.save(showtime);
            mav.addObject("message", "Showtime added successfully!");
            
        } catch (Exception e) {
            mav.addObject("message", "Check all your inputs and try again!");

        }
        
        mav.addObject("showtime", showtime);
        List<Hall> halls = hallRepository.findAll();
        mav.addObject("halls", halls);
        // if (BindingResult.hasErrors()) {
            // mav.addObject("message", "you fucked up");
            // }
            return mav;
    }
    // Edit Showtime
    
    @GetMapping("/edit_showtime")
    public ModelAndView ShowtimeList() {
        ModelAndView mav = new ModelAndView();
        List<Showtime> showtimes = showtimeRepository.findAll();
        mav.addObject("showtimes", showtimes);
        // List<Hall> halls = hallRepository.findAll();
        // mav.addObject("halls", halls);
        mav.setViewName("/admin/edit_showtime");
        return mav;

    }
    
    @GetMapping("/edit_showtime_single/{id}")
    public ModelAndView EditShowtimePage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_showtime_single");
        Optional<Showtime> showtime = showtimeRepository.findById(id);
        if (showtime.isPresent()) {
            
            mav.addObject("showtime", showtime.get());
            
            
        } else {
            mav.addObject("message", "No showtime with that ID in the database!!");
        }
        List<Hall> halls = hallRepository.findAll();
        mav.addObject("halls", halls);
        mav.addObject("ShowtimeMaps", ShowtimeMaps.class);
        return mav;
    }
    
    @PostMapping("/edit_showtime_single")
    public ModelAndView EditShowtimeAction(@Valid Showtime showtime, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_showtime_single");
        
        Optional<Showtime> origShowtime = showtimeRepository.findById(showtime.getId());
        if (origShowtime.isPresent()) {
            try {
                origShowtime.get().setName(showtime.getName());
                origShowtime.get().setDate(showtime.getDate());
                origShowtime.get().setTime(showtime.getTime());
                origShowtime.get().setAvailability(showtime.getAvailability());
                Hall hall = hallRepository.findById(showtime.getHall_id()).get();
                origShowtime.get().setHall(hall);
                origShowtime.get().setAvailability(1);
                showtimeRepository.save(origShowtime.get());
                mav.addObject("message", "Showtime edited successfully!");

            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
            mav.addObject("showtime", origShowtime.get());
        } else {
            mav.addObject("message", "No showtime with that ID in the database!!");
        }
        mav.addObject("ShowtimeMaps", ShowtimeMaps.class);

        return mav;
    }

    // Delete Showtime

    @GetMapping("/delete_showtime/{id}")
    public ModelAndView DeleteShowtimeAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_showtime");
        Optional<Showtime> showtime = showtimeRepository.findById(id);
        if (showtime.isPresent()) {
            try {
                showtimeRepository.delete(showtime.get());
                mav.addObject("message", "Showtime deleted successfully!!");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());

            }

        } else {
            mav.addObject("message", "No showtime with that ID in the database!!");
        }
        List<Showtime> showtimes = showtimeRepository.findAll();
        mav.addObject("showtimes", showtimes);
        return mav;
    }

    // Edit Hall

    @GetMapping("/edit_hall")
    public ModelAndView HallList(Model model) {
        ModelAndView mav = new ModelAndView();
        List<Hall> halls = hallRepository.findAll();
        mav.addObject("halls", halls);
        mav.setViewName("/admin/edit_hall");
        // System.out.println(MovieMaps.genreMap.get(1));

        return mav;
    }

    @GetMapping("/edit_hall_single/{id}")
    public ModelAndView EditHallPage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_hall_single");

        Optional<Hall> hall = hallRepository.findById(id);
        if (hall.isPresent()) {
            mav.addObject("hall", hall);
        } else {
            mav.addObject("message", "No hall with that ID in the database!!");
        }
        mav.addObject("HallMaps", HallMaps.class);

        return mav;
    }

    @PostMapping("/edit_hall_single")
    public ModelAndView EditHallAction(@Valid Hall hall, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_hall_single");

        Optional<Hall> origHall = hallRepository.findById(hall.getId());
        if (origHall.isPresent()) {
            try {
                origHall.get().setName(hall.getName());
                origHall.get().setCapacity(hall.getCapacity());
                origHall.get().setStatus(hall.getStatus());

                hallRepository.save(origHall.get());
                mav.addObject("message", "Hall edited successfully!");

            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
            mav.addObject("hall", origHall.get());
        } else {
            mav.addObject("message", "No hall with that ID in the database!!");
        }
        mav.addObject("HallMaps", HallMaps.class);

        return mav;
    }

    // Delete Hall

    @GetMapping("/delete_hall/{id}")
    public ModelAndView DeleteHallAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_hall");
        Optional<Hall> hall = hallRepository.findById(id);
        if (hall.isPresent()) {
            try {
                hallRepository.delete(hall.get());
                mav.addObject("message", "Hall deleted successfully!!");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());

            }

        } else {
            mav.addObject("message", "No hall with that ID in the database!!");
        }
        List<Hall> halls = hallRepository.findAll();
        mav.addObject("halls", halls);
        return mav;
    }

    // Add News

    @GetMapping("/add_news")
    public ModelAndView AddNewsPage() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("news", new News());
        mav.setViewName("/admin/add_news");
        return mav;
    }

    @PostMapping("/add_news")
    public ModelAndView AddNewsAction(@Valid News news, BindingResult BindingResult) {
        ModelAndView mav = new ModelAndView();

        try {
            newsRepository.save(news);
            mav.addObject("message", "News added successfully!");

        } catch (Exception e) {
            mav.addObject("message", "Check all your inputs and try again!");

        }
        mav.setViewName("/admin/add_news");
        return mav;
    }

    // Edit News

    @GetMapping("/edit_news")
    public ModelAndView NewsList(Model model) {
        ModelAndView mav = new ModelAndView();
        List<News> newss = newsRepository.findAll();
        mav.addObject("newss", newss);
        mav.setViewName("/admin/edit_news");

        return mav;
    }

    @GetMapping("/edit_news_single/{id}")
    public ModelAndView EditNewsPage(@PathVariable int id) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_news_single");

        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            mav.addObject("news", news);
        } else {
            mav.addObject("message", "No news with that ID in the database!!");
        }

        return mav;
    }

    @PostMapping("/edit_news_single")
    public ModelAndView EditNewsAction(@Valid News news, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_news_single");

        Optional<News> origNews = newsRepository.findById(news.getId());
        if (origNews.isPresent()) {
            try {
                origNews.get().setTitle(news.getTitle());
                origNews.get().setSumm(news.getSumm());
                origNews.get().setDescription(news.getDescription());
                origNews.get().setLink(news.getLink());
                origNews.get().setNewsImages(news.getNewsImages());

                newsRepository.save(origNews.get());
                mav.addObject("message", "News edited successfully!");

            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
            mav.addObject("news", origNews.get());
        } else {
            mav.addObject("message", "No news with that ID in the database!!");
        }

        return mav;
    }

    // Delete News

    @GetMapping("/delete_news/{id}")
    public ModelAndView DeleteNewsAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/admin/edit_news");
        Optional<News> news = newsRepository.findById(id);
        if (news.isPresent()) {
            try {
                newsRepository.delete(news.get());
                mav.addObject("message", "News deleted successfully!!");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());

            }

        } else {
            mav.addObject("message", "No news with that ID in the database!!");
        }
        List<News> newss = newsRepository.findAll();
        mav.addObject("newss", newss);
        return mav;
    }

    @GetMapping("/loadtimes")
    public @ResponseBody String loadTimes(@RequestParam("date") String date) {
        String json = "";
        List<Showtime> showtimes = showtimeRepository.findAllByDate(date);
        Map<String, String> myMap = new HashMap<String, String>();
        for (Showtime s : showtimes) {
            myMap.put(String.valueOf(s.getId()), s.getTime() + " "+ s.getHall().getName());
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(myMap);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

}