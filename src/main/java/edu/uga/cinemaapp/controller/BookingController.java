package edu.uga.cinemaapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import edu.uga.cinemaapp.service.Email;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.uga.cinemaapp.model.Bankcard;
import edu.uga.cinemaapp.model.Booking;
import edu.uga.cinemaapp.model.Movie;
import edu.uga.cinemaapp.model.Ordersummary;
import edu.uga.cinemaapp.model.Seat;
import edu.uga.cinemaapp.model.Showtime;
import edu.uga.cinemaapp.model.User;
import edu.uga.cinemaapp.model.Price;
import edu.uga.cinemaapp.repository.BankcardRepository;
import edu.uga.cinemaapp.repository.BookingRepository;
import edu.uga.cinemaapp.repository.MovieRepository;
import edu.uga.cinemaapp.repository.ShowtimeRepository;
import edu.uga.cinemaapp.repository.OrderRepository;
import edu.uga.cinemaapp.repository.PriceRepository;
import edu.uga.cinemaapp.repository.SeatRepository;
import edu.uga.cinemaapp.service.UserService;
import edu.uga.cinemaapp.viewmodel.BookingVM;
import edu.uga.cinemaapp.viewmodel.OrderVM;

import org.springframework.web.bind.annotation.RequestParam;

@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping("/booking")
@Controller
public class BookingController {

    @Autowired
    private UserService userService;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ShowtimeRepository showtimeRepository;

    @Autowired
    BookingRepository bookingRepository;
    
    @Autowired
    BankcardRepository bankcardRepository;

    @Autowired
    SeatRepository seatRepository;
    
    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    PriceRepository priceRepository;

    @GetMapping("/purchase/{id}")
    public ModelAndView purchase(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView mav = new ModelAndView();
        Optional<Movie> movie = movieRepository.findById(id);
        List<Showtime> showtimes = movie.get().getShowtimes();
        mav.addObject("showtimes", showtimes);
        mav.addObject("movie", movie.get());
        mav.setViewName("/home/time_selection");
        return mav;
    }

    @PostMapping("/buyticket")
    public ModelAndView purchaseaction(int showtime_id, int movie_id) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Showtime showtime = showtimeRepository.findById(showtime_id).get();
        // booking.setMovie(movie.get());
        try {

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setShowtime(showtime);
            booking.setStatus(-1);
            bookingRepository.save(booking);

            BookingVM bvm = new BookingVM();
            bvm.setNumAdult(booking.getNumAdult());
            bvm.setId(booking.getId());
            bvm.setNumChild(booking.getNumChild());
            bvm.setNumOld(booking.getNumOld());
            bvm.setShowtime_id(showtime_id);
            mav.addObject("bookingVM", bvm);

        } catch (Exception e) {
            // TODO: handle exception
            mav.addObject("message", e.getMessage());
        }

        mav.setViewName("/home/seats");
        return mav;
    }

    @PostMapping("/selectseats")
    public ModelAndView SelectSeats(BookingVM bvm) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        // here the number of tickets are stored in the booking object
        ModelAndView mav = new ModelAndView();
        Booking booking = bookingRepository.findById(bvm.getId()).get();
        booking.getShowtime();
        booking.setNumAdult(bvm.getNumAdult());
        booking.setNumChild(bvm.getNumChild());
        booking.setNumOld(bvm.getNumOld());
        booking.setUser(user);
        booking.setStatus(-1);
        // booking.setShowtime(showtimeRepository.findById(bvm.getShowtime_id()).get());
        bookingRepository.save(booking);

        // try {
        // bookingRepository.save(booking);
        // } catch (Exception e) {
        // // TODO: handle exception
        // }
        // mav.addObject("attributeName", attributeValue);
        mav.addObject("booking", bvm);
        mav.setViewName("/home/seat_select");
        return mav;
    }
    @PostMapping("/payment")
    public ModelAndView review(int id, int[] seats) {
        ModelAndView mav = new ModelAndView();
        Optional<Booking> booking = bookingRepository.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if (booking.isPresent()) {
            ArrayList<Seat> occupied = new ArrayList<Seat>();
            Showtime showtime = booking.get().getShowtime();
            try {
                Seat seat;
                for (int item : seats) {
                    seat = new Seat();                    
                    seat.setSeatNumber(item);
                    seat.setShowtime(showtime);
                    occupied.add(seat);
                }
                seatRepository.saveAll(occupied);
                booking.get().setSeats(occupied);
            } catch (Exception e) {
                System.out.println("Exception caught: " + e.getMessage());
            }
            Price price = priceRepository.findById(1).get();
            double netprice = booking.get().getNumAdult() * price.getAdult()
            + booking.get().getNumChild() * price.getChild() + booking.get().getNumOld() * price.getElderly();
            double tax = netprice * ((price.getTax())/100.0);
            double totalprice = netprice + tax;
            double adultprice = booking.get().getNumAdult() * price.getAdult();
            double childprice = booking.get().getNumChild() * price.getChild();
            double elderlyprice = booking.get().getNumOld() * price.getElderly();
            try {
                Ordersummary order = new Ordersummary();
                order.setTotal(totalprice);
                order.setAdult(adultprice);
                order.setChild(childprice);
                order.setElderly(elderlyprice);
                order.setBooking(booking.get());
                orderRepository.save(order);                
                booking.get().setStatus(0);
                mav.addObject("order", order);                
                mav.addObject("bankcards", user.getBankCards());
                mav.setViewName("/home/payment");
            } catch (Exception e) {
                mav.addObject("message", e.getMessage());
                mav.setViewName("/home/seat_select");
            }
        } else {
            mav.addObject("message", "Something went wrong in this step!");
            mav.setViewName("/home/seat_select");
        }
        return mav;
    }
    
    @GetMapping("/ordersummary")
    public ModelAndView review(int bankcard_id, int id) {
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        Ordersummary order = orderRepository.findById(id).get();
        order.setBankcardNum(bankcardRepository.findById(bankcard_id).get().getCardNumber());
        OrderVM ovm = new OrderVM();
        ovm.setAdult(order.getBooking().getNumAdult());
        ovm.setChild(order.getBooking().getNumChild());
        ovm.setElderly(order.getBooking().getNumOld());
        ovm.setBankcardnum(order.getBankcardNum());
        ovm.setHallname(order.getBooking().getShowtime().getHall().getName());
        ovm.setDate(order.getBooking().getShowtime().getDate());
        ovm.setTime(order.getBooking().getShowtime().getTime());
        ovm.setFirstname(order.getBooking().getUser().getName());
        ovm.setLastname(order.getBooking().getUser().getLastName());
        ovm.setId(order.getId());
        ovm.setMoviename(order.getBooking().getShowtime().getMovie().getName());
        ovm.setTotalcost(order.getTotal());
        Booking booking = order.getBooking();
        booking.setStatus(1);
        
        List<Integer> seatnumbers = new ArrayList<Integer>();
        int seatsize = order.getBooking().getSeats().size();
        for(int i = 0; i < seatsize; i++){
            Seat singleseat = order.getBooking().getSeats().get(i);
            int seatvalue = singleseat.getSeatNumber();
            seatnumbers.add(seatvalue);
        }
        ovm.setSeatnumbers(seatnumbers);

        Email email = new Email(user.getEmail(), "Ticket Booked Successfully!","Order Summary:\n\nOrder Number: "+ ovm.getId()+"\nFirst Name: "+ ovm.getLastname() +"\nLast Name: "+ ovm.getFirstname() +"\nMovie Name: "+ ovm.getMoviename() +"\nHall Name: "+ ovm.getHallname() +"\nSeat Numbers:" + Arrays.toString(ovm.getSeatnumbers().toArray()) + "\nShow Date and Time " + ovm.getDate() + " "+ ovm.getTime() +"\nOrder Total: "+ ovm.getTotalcost() +"\nAdults: "+ ovm.getAdult() + "\nSeniors: "+ ovm.getElderly() +"\nChildren:" + ovm.getChild() + "\nBank Card Number: " + ovm.getBankcardnum() + "\n");
        email.SendEmail();
        mav.addObject("ordersummary", ovm);
        mav.setViewName("/home/order_summary");
        return mav;  
    }

}