package com.telegrambot.animailsshelter.shelter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalShelter {
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "WORK_SCHEDULE")
    private String workSchedule;
    @Column(name = "CONTACTS")
    private String contacts;
    @Column(name = "SAFETY_ADVICE")
    private String safetyAdvice;
    @Column(name = "SECURITY_CONTACT")
    private String securityContact;
}
