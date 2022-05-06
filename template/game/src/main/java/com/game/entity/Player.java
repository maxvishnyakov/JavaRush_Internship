package com.game.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="name", nullable = false, length = 12)
    private String name;

    @Column(name="title", nullable = false, length = 30)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="race", nullable = false)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(name="profession", nullable = false)
    private Profession profession;

    @Column(name="experience", nullable = false)
    private Integer experience;

    @Column(name="level", nullable = false)
    private Integer level;

    @Column(name="untilNextLevel", nullable = false)
    private Integer untilNextLevel;

    @Column(name="birthday", nullable = false)
    private Date birthday;

    @Column(name="banned", nullable = false)
    private Boolean banned;

    public boolean areAllFieldsFilled() {
        if (this.name == null
                || this.title == null
                || this.race == null
                || this.profession == null
                || this.birthday == null
                || this.experience == null) return false;
        return true;
    }

    public void ignoringUnnecessaryData() {
        this.id = null;
        this.untilNextLevel = null;
        this.level = null;
    }

    public void updatingWith(Player updatedFields) {
        updatedFields.id = null;
        updatedFields.level = null;
        updatedFields.untilNextLevel = null;
        if (updatedFields.name != null) this.name = updatedFields.name;
        if (updatedFields.title != null) this.title = updatedFields.title;
        if (updatedFields.race != null) this.race = updatedFields.race;
        if (updatedFields.profession != null) this.profession = updatedFields.profession;
        if (updatedFields.birthday != null) this.birthday = updatedFields.birthday;
        if (updatedFields.banned != null) this.banned = updatedFields.banned;
        if (updatedFields.experience != null) {
            this.experience = updatedFields.experience;
            setLevelAndUntilNext(this.experience);
        }
    }

    public void setLevelAndUntilNext(Integer experience) {
        Integer currentLevel = ((int)Math.sqrt(2500+200*experience) - 50)/100;
        Integer expUntil = 50*(currentLevel+1)*(currentLevel+2)- experience;
        this.level = currentLevel;
        this.untilNextLevel = expUntil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (title != null ? !title.equals(player.title) : player.title != null) return false;
        if (race != null ? !race.equals(player.race) : player.race != null) return false;
        if (profession != null ? !profession.equals(player.profession) : player.profession != null) return false;
        if (birthday != null ? !birthday.equals(player.birthday) : player.birthday != null) return false;
        if (banned != null ? !banned.equals(player.banned) : player.banned != null) return false;
        if (experience != null ? !experience.equals(player.experience) : player.experience != null) return false;
        if (level != null ? !level.equals(player.level) : player.level != null) return false;
        if (untilNextLevel != null ? !untilNextLevel.equals(player.untilNextLevel) : player.untilNextLevel != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (profession != null ? profession.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (banned != null ? banned.hashCode() : 0);
        result = 31 * result + (experience != null ? experience.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (untilNextLevel != null ? untilNextLevel.hashCode() : 0);
        return result;
    }


    public Player(Long id,String name, String title, Race race, Profession profession, Integer experience,
                  Integer level, Integer untilNextLevel, Date birthday, Boolean banned) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
        this.birthday = birthday;
        this.banned = banned;
    }

    public Player() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
