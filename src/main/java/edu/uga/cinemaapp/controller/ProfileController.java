package edu.uga.cinemaapp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uga.cinemaapp.model.Bankcard;
import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.repository.BankcardRepository;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.UserRepository;
import edu.uga.cinemaapp.service.UserService;
import edu.uga.cinemaapp.viewmodel.ResetPasswdVM;

@RestController
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/profile")
public class ProfileController {
    // @Autowired
    // BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BankcardRepository bankcardRepository;

    @Autowired
    MovieRepository movieRepository;

    @RequestMapping(value = { "", "/", "/userprofile" }, method = RequestMethod.GET)
    public ModelAndView UserProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ModelAndView mav = new ModelAndView();
        List<Bankcard> bankcards = user.getBankCards();
        mav.addObject("carddetails", bankcards.subList(0,2));
        mav.addObject("passwdvm", new ResetPasswdVM());
        mav.addObject("user", user);
        mav.addObject("card", new Bankcard());
        mav.setViewName("userprofile");
        return mav;
    }

    @RequestMapping(value = "/addcard", method = RequestMethod.POST)
    public ModelAndView BankCardPage(@Valid Bankcard card, BindingResult BindingResult) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        try {
            card.setUser(user);
            bankcardRepository.save(card);
            mav.addObject("message", "Card added successfully!");

        } catch (Exception e) {
            mav.addObject("message", "Check all your inputs and try again!");

        }
        List<Bankcard> carddetails = user.getBankCards();
        mav.addObject("carddetails", carddetails.subList(0,2));
        mav.addObject("card", new Bankcard());
        mav.addObject("passwdvm", new ResetPasswdVM());

        mav.addObject("user", user);

        mav.setViewName("userprofile");
        return mav;
    }

    @GetMapping("/delete_card/{id}")
    public ModelAndView DeleteBankCardAction(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Bankcard> card = bankcardRepository.findById(id);
        if (card.isPresent()) {
            try {
                Bankcard thisCard = card.get();
                // thisCard.setUser(user);
                bankcardRepository.delete(thisCard);
                // user.setBankCards(bankcardRepository);
                // user.setBankCards(user.getBankCards());
                mav.addObject("message", "Card deleted successfully!!");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
            }
        } else {
            mav.addObject("message", "No card with that ID in the database!!");
        }
        User user = userService.findUserByEmail(auth.getName());
        if (user.getBankCards() != null) {
            List<Bankcard> carddetails = user.getBankCards();
            mav.addObject("carddetails", carddetails.subList(0,2));
        }
        mav.addObject("card", new Bankcard());
        mav.addObject("passwdvm", new ResetPasswdVM());
        mav.addObject("user", user);
        mav.setViewName("userprofile");
        return mav;
    }

    @RequestMapping(value = "/updateprofile", method = RequestMethod.POST)
    public ModelAndView UpdateProfile(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User theUser = userService.findUserByEmail(auth.getName());
        try {

            theUser.setPromo(user.getPromo());
            theUser.setDob(user.getDob());
            theUser.setLastName(user.getLastName());
            theUser.setState(user.getState());
            theUser.setName(user.getName());
            theUser.setCountry(user.getCountry());
            theUser.setAddress(user.getAddress());
            userRepository.save(theUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        ModelAndView mav = new ModelAndView();
        List<Bankcard> carddetails = user.getBankCards();
        mav.addObject("carddetails", carddetails.subList(0,2));
        mav.addObject("user", theUser);
        mav.addObject("passwdvm", new ResetPasswdVM());
        mav.addObject("card", new Bankcard());
        mav.setViewName("userprofile");
        return mav;
    }

    @RequestMapping(value = "/updatepassword", method = RequestMethod.POST)
    public ModelAndView ResetPassword(ResetPasswdVM rpvm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User theUser = userService.findUserByEmail(auth.getName());
        ModelAndView mav = new ModelAndView();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            String three = rpvm.getNewPasswd();
            String four = rpvm.getConfPasswd();
            if (!three.equals(four) || !encoder.matches(rpvm.getOldPasswd(), theUser.getPassword())) {
                mav.addObject("message", "you fucked up bro");
            } else {
                theUser.setPassword(encoder.encode(rpvm.getNewPasswd()));
                userRepository.save(theUser);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        List<Bankcard> carddetails = theUser.getBankCards();
        mav.addObject("carddetails", carddetails.subList(0,2));
        mav.addObject("user", theUser);
        mav.addObject("passwdvm", new ResetPasswdVM());
        mav.addObject("card", new Bankcard());
        mav.setViewName("userprofile");
        return mav;
    }

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