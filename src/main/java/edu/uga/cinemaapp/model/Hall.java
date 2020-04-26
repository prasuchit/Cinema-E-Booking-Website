package edu.uga.cinemaapp.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hall", uniqueConstraints = { @UniqueConstraint(columnNames = { "name"}) })
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hall_id")
    private int id;

    // @NotEmpty sets the validation constraint that the value of "name" should not
    // be null.
    // it also works in thymeleaf too! i.e. if user leaves the "name" blank an error
    // will be shown above the "name" attribute telling the user: *Please provide
    // your name
    // using something like this: th:if="${#fields.hasErrors('name')}"
    @NotEmpty(message = "*Please provide hall name")
    @Column(name = "name")
    private String name;

    private int capacity;

    private int status;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;

}