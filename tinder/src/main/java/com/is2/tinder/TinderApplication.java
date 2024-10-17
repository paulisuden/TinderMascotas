package com.is2.tinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.is2.tinder.business.logic.service.ZonaService;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class TinderApplication implements CommandLineRunner {
	@Autowired
	ZonaService zonaService;

	public static void main(String[] args) {
		SpringApplication.run(TinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			zonaService.crearZona("Lujan de cuyo", "Si");
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Running");

	}

}
