package org.example.assessmentjackpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class AssessmentJackpotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssessmentJackpotApplication.class, args);
    }

}
