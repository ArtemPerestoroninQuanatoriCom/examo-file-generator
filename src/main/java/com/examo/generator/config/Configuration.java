package com.examo.generator.config;

import lombok.Data;

@Data
public class Configuration {

    private Integer examsCount = 20;
    private Integer minQuestionsInGroup = 1000;
    private Integer maxQuestionsInGroup = 1500;
    private Integer minQuestionsForTemplate = 10;
    private Integer maxQuestionsForTemplate = 30;
    private Integer minAnswersCount = 3;
    private Integer maxAnswersCount = 6;

    private String outputDir = "target/generated/";

}
