package com.game.controller;

import com.game.entity.Player;
import com.game.repository.specifications.PlayerSpecs;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value ="/rest/players")
    public List<Player> showPlayers(Model model, @RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Specification<Player> spec = Specification.where(null);
        if(name != null) {
            spec = spec.and(PlayerSpecs.nameContains(name));
        }
        List<Player> resultList = playerService.getPlayersWithSpecs(spec, PageRequest.of(pageNumber, pageSize)).getContent();
        model.addAttribute("mainTable", resultList);
        return resultList;
    }
}