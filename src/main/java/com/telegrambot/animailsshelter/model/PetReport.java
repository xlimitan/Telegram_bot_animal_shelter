package com.telegrambot.animailsshelter.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 * Класс PetReport представляет информацию об отчёте животного. Эта информация сохраняется в базе данных.
 */
@Getter
@Setter
@Entity
@Table(name = "pet_report")
public class PetReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "diet")
    private String diet;
    @Column(name = "feelings")
    private String feelings;
    @Column(name = "behaviour")
    private String behaviour;
    @Column(name = "check_inf")
    private boolean checkInf;
    @Column(name = "date")
    private LocalDateTime date;
    @OneToOne
    @JoinColumn(name = "animalowner_id")
    private AnimalOwner animalOwner;

    public PetReport(long id,String diet, String feelings, boolean checkInf, LocalDateTime date) {
        this.id = id;
        this.diet = diet;
        this.feelings = feelings;
        this.checkInf = checkInf;
        this.date = date;
    }
    public PetReport() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetReport petReport = (PetReport) o;
        return id == petReport.id && checkInf == petReport.checkInf && Objects.equals(diet, petReport.diet) && Objects.equals(feelings, petReport.feelings) && Objects.equals(behaviour, petReport.behaviour) && Objects.equals(date, petReport.date) && Objects.equals(animalOwner, petReport.animalOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, diet, feelings, behaviour, checkInf, date, animalOwner);
    }

    @Override
    public String toString() {
        return "PetReport{" +
                "id=" + id +
                ", diet='" + diet + '\'' +
                ", feelings='" + feelings + '\'' +
                ", behaviour='" + behaviour + '\'' +
                ", check=" + checkInf +
                ", date=" + date +
                ", animalOwner=" + animalOwner +
                '}';
    }
}
