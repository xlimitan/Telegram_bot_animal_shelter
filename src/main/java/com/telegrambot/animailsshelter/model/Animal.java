package com.telegrambot.animailsshelter.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
@Getter
@Setter
@Entity
/**
 * Класс Animal представляет информацию о животном. Эта информация сохраняется в базе данных.
 */
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "animal_type")
    private String animalType;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "breed")
    private String breed;
    @Column(name = "path_to_photo")
    private String pathToPhoto;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;

    public Animal() {

    }
    public Animal( long id,String animalType, String name, int age, String breed) {
        this.id = id;
        this.animalType = animalType;
        this.name = name;
        this.age = age;
        this.breed = breed;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id && age == animal.age && Objects.equals(animalType, animal.animalType) && Objects.equals(name, animal.name) && Objects.equals(breed, animal.breed) && Objects.equals(pathToPhoto, animal.pathToPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, animalType, name, age, breed, pathToPhoto);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", animalType='" + animalType + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", breed='" + breed + '\'' +
                '}';
    }
}
