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
    @Column(name = "name")
    private String name;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "e_mail")
    private String eMail;
    @Column(name = "trial_period")
    private boolean trialPeriod;
    public AnimalOwner(String name, String phoneNumber, String eMail, boolean trialPeriod) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        this.trialPeriod = trialPeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalOwner that = (AnimalOwner) o;
        return id == that.id && trialPeriod == that.trialPeriod && Objects.equals(name, that.name) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(eMail, that.eMail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber, eMail, trialPeriod);
    }

    @Override
    public String toString() {
        return "AnimalOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", eMail='" + eMail + '\'' +
                ", trialPeriod=" + trialPeriod +
                '}';
    }
}
