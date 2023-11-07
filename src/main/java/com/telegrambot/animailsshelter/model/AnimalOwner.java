package com.telegrambot.animailsshelter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
/**
 * Класс AnimalOwner представляет информацию о владельце животного. Эта информация сохраняется в базе данных.
 */
@Entity
@Getter
@Setter
@Table(name = "animal_owner")
public class AnimalOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "trial_period")
    private boolean trialPeriod;
    public AnimalOwner(long id, boolean trialPeriod) {
        this.id = id;
        this.trialPeriod = trialPeriod;
    }

    public AnimalOwner() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalOwner that = (AnimalOwner) o;
        return id == that.id && trialPeriod == that.trialPeriod;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trialPeriod);
    }

    @Override
    public String toString() {
        return "AnimalOwner{" +
                "id=" + id +
                ", trialPeriod=" + trialPeriod +
                '}';
    }
}
