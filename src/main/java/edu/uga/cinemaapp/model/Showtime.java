package edu.uga.cinemaapp.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.WhereJoinTable;
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
@Table(name = "showtime", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "date", "time" }) })
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id")
    private int id;

    // @NotEmpty sets the validation constraint that the value of "name" should not
    // be null.
    // it also works in thymeleaf too! i.e. if user leaves the "name" blank an error
    // will be shown above the "name" attribute telling the user: *Please provide
    // your name
    // using something like this: th:if="${#fields.hasErrors('name')}"
    @NotEmpty(message = "*Please provide a valid name")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "*Please provide a valid Date")
    @Column(name = "date")
    private String date;

    @NotEmpty(message = "*Please provide a valid time")
    @Column(name = "time")
    private String time;

    @NotEmpty(message = "*Please provide a valid duration")
    @Column(name = "duration")
    private String duration;

    @NotNull(message = "*Please provide a valid availability")
    @Column(name = "availability")
    private int availability;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @Transient
    private int hall_id;
}