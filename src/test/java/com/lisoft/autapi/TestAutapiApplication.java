package com.lisoft.autapi;

import org.springframework.boot.SpringApplication;

public class TestAutapiApplication {

	public static void main(String[] args) {
		SpringApplication.from(AutapiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
