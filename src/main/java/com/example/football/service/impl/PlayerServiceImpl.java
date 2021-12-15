package com.example.football.service.impl;

import com.example.football.models.dto.PlayerRootSeedDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";
    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final TownService townService;
    private final TeamService teamService;
    private  final StatService statService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, TownService townService, TeamService teamService, StatService statService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
    }

    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {

        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

      xmlParser
              .fromFile(PLAYERS_FILE_PATH, PlayerRootSeedDto.class)
              .getPlayers()
              .stream()
              .filter(playerSeedDto -> {
                  boolean isValid = validationUtil.isValid(playerSeedDto);
                  sb.append(isValid
                  ? String.format("Successfully imported Player %s %s - %s",
                          playerSeedDto.getFirstName(), playerSeedDto.getLastName(),
                          playerSeedDto.getPosition())
                  : "Invalid Player").append(System.lineSeparator());
                  return isValid;
              }).map(playerSeedDto -> {
          Player player = modelMapper.map(playerSeedDto, Player.class);
          player.setTown(townService.findTownByName(playerSeedDto.getTownName().getName()));
          player.setTeam(teamService.findTeamByName(playerSeedDto.getTeam().getName()));
          player.setStat(statService.findStatById(playerSeedDto.getStat().getId()));
          return player;
      }).forEach(playerRepository::save);
        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
      StringBuilder sb = new StringBuilder();
       playerRepository.findPlayersByBirthDateBetweenOrderByShootingDescThenByPassingDescThenByEnduranceDescThenByPlayerLastName(LocalDate.of(1995, 1, 1), LocalDate.of(2003,1, 1 ))
               .forEach(player -> {
                   sb.append(String.format("Player - %s %s%n\tPosition - %s%n\tTeam - %s%n\tStadium - %s%n",
                           player.getFirstName(),player.getLastName(),player.getPosition(),
                           player.getTeam().getName(), player.getTeam().getStadiumName()));
               });
        return sb.toString();
    }
}
