package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.specifications.PlayerSpecs;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
public class PlayerController {
    private PlayerService playerService;
    private Specification<Player> spec;

    private Integer PAGE_NUMBER = 0;

    private Integer PAGE_SIZE = 3;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players")
    public List<Player> showPlayers(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "race", required = false) Race race,
                                    @RequestParam(value = "profession", required = false) Profession profession,
                                    @RequestParam(value = "after", required = false) Long after,
                                    @RequestParam(value = "before", required = false) Long before,
                                    @RequestParam(value = "banned", required = false) Boolean banned,
                                    @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                    @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                    @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                    @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                    @RequestParam(value = "order", required = false) PlayerOrder order,
                                    @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        spec = Specification.where(null);
        if (name != null) {
            spec = spec.and(PlayerSpecs.nameContains(name));
        }
        if (title != null) {
            spec = spec.and(PlayerSpecs.titleContains(title));
        }
        if (race != null) {
            spec = spec.and(PlayerSpecs.findByRaceEquals(race));
        }
        if (profession != null) {
            spec = spec.and(PlayerSpecs.findByProfessionEquals(profession));
        }
        if (after != null) {
            spec = spec.and(PlayerSpecs.findDateAfter(after));
        }
        if (before != null) {
            spec = spec.and(PlayerSpecs.findDateBefore(before));
        }
        if(banned != null) {
            spec = spec.and(PlayerSpecs.banned(banned));
        }
        if(minExperience != null) {
            spec = spec.and(PlayerSpecs.findGreaterOrEqualExperience(minExperience));
        }
        if(maxExperience != null) {
            spec = spec.and(PlayerSpecs.findLessOrEqualExperience(maxExperience));
        }
        if(minLevel != null) {
            spec = spec.and(PlayerSpecs.findGreaterOrEqualLevel(minLevel));
        }
        if(maxLevel != null) {
            spec = spec.and(PlayerSpecs.findLessOrEqualLevel(maxLevel));
        }
        if(pageNumber == null) {
            pageNumber = this.PAGE_NUMBER;
        }
        if(pageSize == null) {
            pageSize = this.PAGE_SIZE;
        }
        if(order == null) {
            order = PlayerOrder.ID;
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        List<Player> resultList = playerService.getPlayersWithSpecs(spec, pageable).getContent();
        return resultList;
    }

    @GetMapping(value ="/rest/players/count")
    public Integer getCount(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "race", required = false) Race race,
                            @RequestParam(value = "profession", required = false) Profession profession,
                            @RequestParam(value = "after", required = false) Long after,
                            @RequestParam(value = "before", required = false) Long before,
                            @RequestParam(value = "banned", required = false) Boolean banned,
                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel){
        Specification<Player> spec = Specification.where(null);
        if(name != null) {
            spec = spec.and(PlayerSpecs.nameContains(name));
        }
        if(title != null) {
            spec = spec.and(PlayerSpecs.titleContains(title));
        }
        if(race != null) {
            spec = spec.and(PlayerSpecs.findByRaceEquals(race));
        }
        if(profession != null) {
            spec = spec.and(PlayerSpecs.findByProfessionEquals(profession));
        }
        if (after != null) {
            spec = spec.and(PlayerSpecs.findDateAfter(after));
        }
        if (before != null) {
            spec = spec.and(PlayerSpecs.findDateBefore(before));
        }
        if(banned != null) {
            spec = spec.and(PlayerSpecs.banned(banned));
        }
        if(minExperience != null) {
            spec = spec.and(PlayerSpecs.findGreaterOrEqualExperience(minExperience));
        }
        if(maxExperience != null) {
            spec = spec.and(PlayerSpecs.findLessOrEqualExperience(maxExperience));
        }
        if(minLevel != null) {
            spec = spec.and(PlayerSpecs.findGreaterOrEqualLevel(minLevel));
        }
        if(maxLevel != null) {
            spec = spec.and(PlayerSpecs.findLessOrEqualLevel(maxLevel));
        }
        Integer  resultCount = playerService.countAll(spec);
        return resultCount;
    }

    @PostMapping("/rest/players")
    public Player createPlayer(@RequestBody Player player){
        if(!player.areAllFieldsFilled()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(playerService.isNewObjectValid(player) == true) {
            player.ignoringUnnecessaryData();
            playerService.createPlayer(player);
            return player;
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/rest/players/{id}")
    public Player getPlayer(@PathVariable(name = "id") Long id) {
        if(id % 1 != 0 || id <= 0 || id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional <Player> player = playerService.getPlayer(id);
        if(player.isPresent()) {
            return player.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/rest/players/{id}")
    public Player updatePlayer(@PathVariable(name = "id") Long id, @RequestBody Player updatedPlayer) {
        Player player = getPlayer(id);
        player.updatingWith(updatedPlayer);
        if (playerService.isNewObjectValid(player) == true && player.areAllFieldsFilled())  {
            playerService.updatePlayer(player);
            return player;
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping(value = "/rest/players/{id}")
    public void deletePlayer(@PathVariable(name = "id") Long id) {
        if(id % 1 != 0 || id <= 0 || id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Player player = getPlayer(id);
        if(player != null) {
            playerService.deletePlayer(id);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}