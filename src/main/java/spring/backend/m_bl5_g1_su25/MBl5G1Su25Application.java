package spring.backend.m_bl5_g1_su25;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MBl5G1Su25Application {
	@RequestMapping("/")
	 String index() {
		return "Hello World";
	}
	public static void main(String[] args) {
		SpringApplication.run(MBl5G1Su25Application.class, args);


	}

}
