package me.duanbn.snailfish.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import me.duanbn.snailfish.springboot.starter.EnableSnailfishFramework;

@SpringBootApplication
@EnableSnailfishFramework(scanPackages = "me.duanbn.snailfish.test", enableDDL = true, enableLog = true, enableSQLLog = true)
// @EnableSnailfishRpc
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
