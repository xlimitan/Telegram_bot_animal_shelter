package com.telegrambot.animailsshelter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Класс User представляет информацию о пользователе бота. Эта информация сохраняется в базе данных.
 */
@Setter
@Getter
@Entity(name = "usersDataTable")
public class User {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private String firstName;

    private String lastName;

    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "e_Mail")
    private String eMail;

     @OneToOne
     @JoinColumn(name = "animal_id")
     private Animal animal;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "trial_period")
    private boolean trialPeriod;
    public User() {
    }

    public User (Long chatId, String firstName, String lastName, String userName,String phoneNumber,String eMail,Animal animal, LocalDate data, boolean trialPeriod) {
        setChatId(chatId);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
        setPhoneNumber(phoneNumber);
        seteMail(eMail);
        setAnimalId(animal);
        setDate(data);
        setTrialPeriod(trialPeriod);
    }


    public void setAnimalId(Animal animalId) {
        this.animal = animalId;
    }


    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String geteMail() {
        return eMail;
    }

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
