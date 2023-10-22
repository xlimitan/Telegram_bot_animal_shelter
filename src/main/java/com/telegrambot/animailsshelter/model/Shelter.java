package com.telegrambot.animailsshelter.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
/**
 * Класс Shelter представляет информацию о приюте. Эта информация сохраняется в базе данных.
 */
@Entity
@Getter
@Setter
@Table(name = "shelter")
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "shelter_type")
    private String shelterType;
    @Column(name = "shelter_name")
    private String shelterName;
    @Column(name = "address")
    private String address;
    @Column(name = "information")
    private String information;

    @OneToMany(mappedBy = "shelter")
    private Collection<Animal> animals;

    public Shelter(String shelterType, String shelterName, String address, String information) {
        this.shelterType = shelterType;
        this.shelterName = shelterName;
        this.address = address;
        this.information = information;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shelter shelter = (Shelter) o;
        return id == shelter.id && Objects.equals(shelterType, shelter.shelterType) && Objects.equals(shelterName, shelter.shelterName) && Objects.equals(address, shelter.address) && Objects.equals(information, shelter.information) && Objects.equals(animals, shelter.animals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shelterType, shelterName, address, information, animals);
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "id=" + id +
                ", shelterType='" + shelterType + '\'' +
                ", shelterName='" + shelterName + '\'' +
                ", address='" + address + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
