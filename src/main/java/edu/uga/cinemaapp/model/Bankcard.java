package edu.uga.cinemaapp.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "bankcard", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "cardNumber", "cvv" }) })
public class Bankcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private int id;

    private String cardNumber;

    // @NotEmpty sets the validation constraint that the value of "name" should not
    // be null.
    // it also works in thymeleaf too! i.e. if user leaves the "name" blank an error
    // will be shown above the "name" attribute telling the user: *Please provide
    // your name
    // using something like this: th:if="${#fields.hasErrors('name')}"
    @NotEmpty(message = "*Please provide cardholder's name")
    @Column(name = "name")
    private String name;

    private String cvv;

    private String expdate;

    // @JoinColumn(name = "user_id")
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}