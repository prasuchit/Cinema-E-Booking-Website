package edu.uga.cinemaapp.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.Role;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.model.News;
import edu.uga.cinemaapp.repository.NewsRepository;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.RoleRepository;
import edu.uga.cinemaapp.repository.UserRepository;
import edu.uga.cinemaapp.service.Email;
import edu.uga.cinemaapp.service.MD5;
import edu.uga.cinemaapp.service.UserService;
import edu.uga.cinemaapp.viewmodel.ForgotPasswdVM;
import edu.uga.cinemaapp.viewmodel.ResetPasswdVM;

@Controller
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    NewsRepository newsRepository;

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public ModelAndView loginPage(User user2) {

        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("log_req", true);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
    public ModelAndView login(@Valid User user, BindingResult bindingResult) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", user);
        List<Movie> movies = movieRepository.findAll();
        mav.addObject("movies", movies);
        mav.addObject("log_req", true);
        if (user.getRole().getId() == 1) {
            mav.setViewName("admin/index");
        } else{
            List<News> newss = newsRepository.findAll();
            mav.addObject("newss", newss);
            mav.setViewName("index");
        }
        return mav;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.addObject("reg_req", true);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        modelAndView.addObject("reg_req", true);
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user",
                    "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message", "Error in registration!");
            modelAndView.setViewName("index");
        } else {
            try {
                user.setHash(MD5.getMd5(user.getEmail() + user.getLastName() + user.getName()));
                Role role = roleRepository.findByRole("ROLE_USER");
                user.setRole(role);
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                modelAndView.addObject("message",
                        "User has been registered successfully \n An email containing activation link has been sent to your email address.");
                modelAndView.addObject("user", new User());
                modelAndView.setViewName("index");
                Email email = new Email(user.getEmail(), "Registration Successful!",
                        "The Activation Link is http://localhost:8080/activate/" + user.getHash());
                email.SendEmail();
            } catch (Exception e) {
                userRepository.delete(user);
                modelAndView.addObject("message", "Error occured!" + e.getMessage());
            }

        }
        return modelAndView;
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        // modelAndView.addObject("userName",
        // "Welcome " + user.getName() + " " + user.getLastName() + " (" +
        // user.getEmail() + ")");
        // modelAndView.addObject("adminMessage", "Content Available Only for Users with
        // Admin Role");
        if (user != null) {
            modelAndView.addObject("login_success", true);

        } else {
            user = new User();
        }
        modelAndView.addObject("user", user);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/activate/{hash}", method = RequestMethod.GET)
    public ModelAndView Activate_User(@PathVariable String hash) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userRepository.findByHash(hash);
        modelAndView.setViewName("index");
        modelAndView.addObject("log_req", true);
        if (user != null) {
            user.setActive(1);
            userRepository.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("message", "User successfully activated!");
        } else {
            modelAndView.addObject("user", new User());
            modelAndView.addObject("message", "User not found!");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/forgotpasswd", method = RequestMethod.GET)
    public ModelAndView ForgotPasswd() {
        ModelAndView mav = new ModelAndView();

        mav.setViewName("forgotpasswd");
        return mav;
    }

    @RequestMapping(value = "/reqpasswdreset", method = RequestMethod.POST)
    public ModelAndView SendResetPasswdLink(String email) {
        ModelAndView mav = new ModelAndView();

        User user = userRepository.findByEmail(email.toString());
        if (user == null) {
            mav.addObject("message", "No user found with this email!");
            // bindingResult.rejectValue("email", "error.user", "Email not found!");
        } else {

            Email sendEmail = new Email(user.getEmail(), "Reset Password!",
                    "The Activation Link is http://localhost:8080/resetpasswd/" + user.getHash());
            sendEmail.SendEmail();
            mav.addObject("message", "Check your email for the password reset link!");
        }

        mav.setViewName("forgotpasswd");

        return mav;
    }

    @RequestMapping(value = "/resetpasswd/{hash}", method = RequestMethod.GET)
    public ModelAndView SetNewPasswd(@PathVariable String hash) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("setnewpasswd");
        User user = null;
        try {
            user = userRepository.findByHash(hash);
        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
        }
        if (user == null) {
            mav.addObject("message", "Check your email for the password reset link!");
        }
        mav.addObject("fpvm", new ForgotPasswdVM() {
            {
                setHash(hash);
            }
        });
        return mav;
    }

    @RequestMapping(value = "/resetpasswd", method = RequestMethod.POST)
    public ModelAndView SetNewPasswd(@Valid ForgotPasswdVM fpvm) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("setnewpasswd");
        User user = null;
        try {
            user = userRepository.findByHash(fpvm.getHash());

        } catch (Exception e) {
            mav.addObject("message", e.getMessage());
        }
        if (user == null) {
            mav.addObject("message", "No user found!");

        } else if (!fpvm.getNewPass().equals((fpvm.getConfPass()))) {
            mav.addObject("message", "New password and the password confirmation do not match");
        } else {
            try {
                user.setPassword(bCryptPasswordEncoder.encode(fpvm.getNewPass()));
                userRepository.save(user);
                mav.addObject("message", "New password has been set!");

            } catch (Exception e) {
                mav.addObject("message", "Something got fucked!");
            }
        }

        mav.addObject("fpvm", fpvm);
        return mav;
    }

}
