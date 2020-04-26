package edu.uga.cinemaapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private int id;

    // @NotEmpty sets the validation constraint that the value of "name" should not
    // be null.
    // it also works in thymeleaf too! i.e. if user leaves the "name" blank an error
    // will be shown above the "name" attribute telling the user: *Please provide
    // your name
    // using something like this: th:if="${#fields.hasErrors('name')}"
    @NotEmpty(message = "*Please provide news title")
    @Column(name = "title")
    private String title;

    private String summ;

    private String description;

    private String link;

    private String newsImages;

}