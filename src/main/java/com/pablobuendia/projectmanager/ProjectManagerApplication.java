package com.pablobuendia.projectmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectManagerApplication {

	private static final Logger log = LoggerFactory.getLogger(ProjectManagerApplication.class);

	public static void main(String[] args) {
		log.info("Testing Appplication");
		SpringApplication.run(ProjectManagerApplication.class, args);
	}

}
