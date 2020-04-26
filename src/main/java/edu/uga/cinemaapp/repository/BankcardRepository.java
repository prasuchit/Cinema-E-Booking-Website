package edu.uga.cinemaapp.repository;

import edu.uga.cinemaapp.model.Bankcard;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("bankcardRepository")
public interface BankcardRepository extends JpaRepository<Bankcard, Integer> {

    Bankcard findByName(String name);

    // Optional<Bankcard> findById(int id);

    @Query(value = "DELETE FROM BANKCARD BC WHERE BC.cardNumber LIKE %:cardNumber%", nativeQuery = true)
    Boolean deleteByCardNumber(@Param("cardNumber") String cardNumber);

    @Query(value = "SELECT * FROM BANKCARD BC ORDER BY BC.ID ASC LIMIT 3", nativeQuery = true)
    List<Bankcard> findAll();
}