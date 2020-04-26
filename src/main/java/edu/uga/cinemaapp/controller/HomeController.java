package edu.uga.cinemaapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.News;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.other.MovieMaps;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.NewsRepository;
import edu.uga.cinemaapp.service.UserService;

@Controller
// @RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    NewsRepository newsRepository;

    @RequestMapping(value = { "/", "" }, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        mav.addObject("user", new User());
        List<News> newss = newsRepository.findAll();
        mav.addObject("newss", newss);
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/search")
    public ModelAndView Search(String keyword, String searchparam) {
        ModelAndView mav = new ModelAndView();
        if(searchparam.equals("1")){
            List<Movie> movies = movieRepository.searchInName(keyword);
            try {
                if (movies.size() > 0) {
                    mav.addObject("movies", movies);
                    mav.setViewName("/home/movielist");
                } else {
                    mav.addObject("message", "No results!");
                    List<News> newss = newsRepository.findAll();
                    mav.addObject("newss", newss);
                    mav.setViewName("index");
                    mav.addObject("user", new User());
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }    
        }
        else if(searchparam.equals("2")){
            int genre = MovieMaps.genreReverseMap.get(keyword.toLowerCase());
            List<Movie> movies = movieRepository.searchInCategoryList(genre);
            try {
                if (movies.size() > 0) {
                    mav.addObject("movies", movies);
                    mav.setViewName("/home/movielist");
                } else {
                    mav.addObject("message", "No results!");
                    List<News> newss = newsRepository.findAll();
                    mav.addObject("newss", newss);
                    mav.setViewName("index");
                    mav.addObject("user", new User());
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

        else if(searchparam.equals("3")){
            if(keyword.isEmpty()){
                System.out.println("Keyword: "+keyword);
                List<Movie> movies = movieRepository.findComingSoon();
                try {
                    if (movies.size() > 0) {
                        mav.addObject("movies", movies);
                        mav.setViewName("/home/movielist");
                    } else {
                        mav.addObject("message", "No results!");
                        List<News> newss = newsRepository.findAll();
                        mav.addObject("newss", newss);
                        mav.setViewName("index");
                        mav.addObject("user", new User());
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }
            }
        else{
            System.out.println("Keyword: "+keyword);
            List<Movie> movies = movieRepository.searchInName(keyword);
            try {
                if (movies.size() > 0) {
                    mav.addObject("movies", movies);
                    mav.setViewName("/home/movielist");
                } else {
                    mav.addObject("message", "No results!");
                    List<News> newss = newsRepository.findAll();
                    mav.addObject("newss", newss);
                    mav.setViewName("index");
                    mav.addObject("user", new User());
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }  
        }
    }

        else if(searchparam.equals("4")){
            if(keyword.isEmpty()){
                    System.out.println("Keyword: "+keyword);
                    List<Movie> movies = movieRepository.findAvailable();
                    try {
                        if (movies.size() > 0) {
                            mav.addObject("movies", movies);
                            mav.setViewName("/home/movielist");
                        } else {
                            mav.addObject("message", "No results!");
                            List<News> newss = newsRepository.findAll();
                            mav.addObject("newss", newss);
                            mav.setViewName("index");
                            mav.addObject("user", new User());
                        }
                    } catch (Exception e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
            else{
                System.out.println("Keyword: "+keyword);
                List<Movie> movies = movieRepository.searchInName(keyword);
                try {
                    if (movies.size() > 0) {
                        mav.addObject("movies", movies);
                        mav.setViewName("/home/movielist");
                    } else {
                        mav.addObject("message", "No results!");
                        List<News> newss = newsRepository.findAll();
                        mav.addObject("newss", newss);
                        mav.setViewName("index");
                        mav.addObject("user", new User());
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                }  
            }
        }
        return mav;
    }    


    @GetMapping("/error")
    public ModelAndView ErrorPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/404");
        return mav;
    }

    @GetMapping("/blank")
    public ModelAndView BlankPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/blank");
        return mav;
    }

    @GetMapping("/charts")
    public ModelAndView ChartsPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/charts");
        return mav;
    }

    @GetMapping("/tables")
    public ModelAndView TablesPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/tables");
        return mav;
    }

}