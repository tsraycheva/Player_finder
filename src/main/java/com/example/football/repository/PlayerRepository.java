package com.example.football.repository;

import com.example.football.models.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
//    List<Player> findPlayersByBirthDateBetween(LocalDate afterDate, LocalDate beforeDate);
//
//
//    @Query("SELECT p FROM Player p ORDER BY p.stat.shooting DESC, p.stat.passing DESC, p.stat.endurance DESC, p.lastName")
//    List<Player> findPlayersByOrderByShootingDescThenByPassingDescThenByEnduranceDescThenByPlayerLastName();

    @Query("SELECT p FROM Player p WHERE p.birthDate > :afterDate AND p.birthDate < :beforeDate ORDER BY p.stat.shooting DESC, p.stat.passing DESC, p.stat.endurance DESC, p.lastName")
    List<Player> findPlayersByBirthDateBetweenOrderByShootingDescThenByPassingDescThenByEnduranceDescThenByPlayerLastName(LocalDate afterDate, LocalDate beforeDate);

}
