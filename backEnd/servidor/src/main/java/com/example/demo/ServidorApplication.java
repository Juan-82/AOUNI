package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class ServidorApplication {

	@RequestMapping("/g")
	public String olaMundo() {
		return "Ola mundo";
	}
	public static void main(String[] args) {
		SpringApplication.run(ServidorApplication.class, args);
	}

}
