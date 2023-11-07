package com.telegrambot.animailsshelter.model;

import liquibase.pro.packaged.S;
import org.glassfish.grizzly.http.util.TimeStamp;

import javax.persistence.*;

/**
 * Класс User представляет информацию о пользователе бота. Эта информация сохраняется в базе данных.
 */
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


    public User() {
    }

    public User( Long chatId, String firstName, String lastName, String userName,String phoneNumber,String eMail) {
        setChatId(chatId);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
        setPhoneNumber(phoneNumber);
        seteMail(eMail);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
