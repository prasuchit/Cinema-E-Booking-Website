package edu.uga.cinemaapp.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "year", "director", "cast" }) })
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int id;

    // @NotEmpty sets the validation constraint that the value of "name" should not
    // be null.
    // it also works in thymeleaf too! i.e. if user leaves the "name" blank an error
    // will be shown above the "name" attribute telling the user: *Please provide
    // your name
    // using something like this: th:if="${#fields.hasErrors('name')}"
    @NotEmpty(message = "*Please provide movie name")
    @Column(name = "name")
    private String name;

    private int status;

    private String cast;

    private int rating;

    private int duration;

    //
    private String genre;

    @Length(min = 10, message = "Length of synopsis too short")
    private String synopsis;
    private String producer;
    private String director;
    private String year;
    private String coverImage;
    private String trailerUrl;

    // @JoinTable(name = "movie_showtime", joinColumns = @JoinColumn(name =
    // "movie_id", referencedColumnName = "movie_id"), inverseJoinColumns =
    // @JoinColumn(name = "showtime_id", referencedColumnName = "showtime_id"))
    // @OneToMany(cascade = CascadeType.ALL)
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;

    // @ManyToMany(cascade = CascadeType.ALL)
    // @JoinTable(name = "movie_hall", joinColumns = @JoinColumn(name = "movie_id",
    // referencedColumnName = "movie_id"), inverseJoinColumns = @JoinColumn(name =
    // "hall_id", referencedColumnName = "hall_id"))
    // private List<Hall> halls;

    @OneToMany(mappedBy = "movie")
    List<Review> reviews;

}