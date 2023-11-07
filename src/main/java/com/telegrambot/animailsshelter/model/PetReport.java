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
    @Column(name = "report")
    private String report;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "correct")
    private boolean correct;

    @OneToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    public PetReport(long id,String report, LocalDateTime date) {
        this.id = id;
        this.date = date;
        this.report= report;
    }
    public PetReport() {
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetReport petReport = (PetReport) o;
        return id == petReport.id && Objects.equals(report, petReport.report) && Objects.equals(date, petReport.date) && Objects.equals(userId, petReport.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, report, date, userId);
    }

    @Override
    public String toString() {
        return "PetReport{" +
                "id=" + id +
                "report=" + report +
                ", date=" + date +
                ", animalOwner=" + userId +
                '}';
    }
}
