package com.compass.pb.mule.testfour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TestFourApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestFourApplication.class, args);
	}

}
