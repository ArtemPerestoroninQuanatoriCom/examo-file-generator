package com.examo.generator.config;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Configuration {

    private Integer examsCount = 20;
    private Integer minQuestionsInGroup = 1000;
    private Integer maxQuestionsInGroup = 1500;
    private Integer minQuestionsForTemplate = 10;
    private Integer maxQuestionsForTemplate = 30;
    private Integer minAnswersCount = 3;
    private Integer maxAnswersCount = 6;
    private Integer maxTemplatesForExam = 2;
    private Integer maxTagsForTemplate = 2;

    private String outputDir = "target/generated/";

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
