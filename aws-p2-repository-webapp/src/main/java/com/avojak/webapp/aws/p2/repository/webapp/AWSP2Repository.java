package com.avojak.webapp.aws.p2.repository.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for AWS p2 Repository.
 */
@ComponentScan(value = {
		"com.avojak.webapp.aws.p2.repository.service",
		"com.avojak.webapp.aws.p2.repository.dataaccess",
		"com.avojak.webapp.aws.p2.repository.webapp" })
@SpringBootApplication
public class AWSP2Repository {

	/**
	 * Launches the Spring Boot application.
	 *
	 * @param args
	 * 		The command-line arguments.
	 */
	public static void main(final String... args) {
		// TODO: Somewhere on startup, should verify that the p2 inspector is running?
		SpringApplication.run(AWSP2Repository.class, args);
	}

}