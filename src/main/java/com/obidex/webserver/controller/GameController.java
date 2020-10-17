package com.obidex.webserver.controller;

import com.obidex.webserver.model.Game;
import com.obidex.webserver.model.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class GameController {
    @Autowired
    private GameRepository repository;

    @GetMapping("/getGames")
    public String getGames(@RequestParam Optional<Integer> start, @RequestParam Optional<Integer> end, Model model) {
        List<Game> games = repository
                .findAll()
                .subList(start.orElse(0), end.orElse(20));
        log.info("Get games reached {}", games);
        games.removeIf(game -> (game.getGameName() == null));
        log.info("Get games reached {}", games);
        games.sort(Comparator.comparing(Game::getGameName));
        model.addAttribute("games", games);
        return "games";
    }

    @GetMapping("/getGame")
    public String getGame(@RequestParam String id, Model model) {
        Game game = repository.findByGameId(id);
        model.addAttribute("game", game);
        log.info("Get Game reached for game: {}", game);
        return "game";
    }
}
