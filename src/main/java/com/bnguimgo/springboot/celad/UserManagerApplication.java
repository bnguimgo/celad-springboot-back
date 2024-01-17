package com.bnguimgo.springboot.celad;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@Log4j2
@SpringBootApplication
@ComponentScan(basePackages = "com.bnguimgo.springboot.celad")
public class UserManagerApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		log.debug("Starting spring boot application class");
		SpringApplication.run(UserManagerApplication.class, args);
	}

}
