package com.telegrambot.animailsshelter.model;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "photo_report")
public class PhotoReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "userId")
    private long userId;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "path")
    private String path;

    public PhotoReport(long userId, LocalDate date, String path) {
        this.userId = userId;
        this.date = date;
        this.path = path;
    }

    public PhotoReport() {

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoReport that = (PhotoReport) o;
        return id == that.id && userId == that.userId && Objects.equals(date, that.date) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, date, path);
    }

    @Override
    public String toString() {
        return "PhotoReport{" +
                "id=" + id +
                ", userId=" + userId +
                ", date=" + date +
                ", path='" + path + '\'' +
                '}';
    }
}
