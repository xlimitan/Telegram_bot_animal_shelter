package com.telegrambot.animailsshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnimailsShelterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimailsShelterApplication.class, args);
	}

}
