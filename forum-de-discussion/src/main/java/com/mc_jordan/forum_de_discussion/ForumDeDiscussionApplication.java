package com.mc_jordan.forum_de_discussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ForumDeDiscussionApplication {

	public static void main(String[] args) {

		SpringApplication.run(ForumDeDiscussionApplication.class, args);
	}

}
