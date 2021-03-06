package com.examo.generator;

import com.examo.generator.config.ArgumentsReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        var configuration = new ArgumentsReader().readArgs(args);

        var examFileGenerator = new ExamFileGenerator(configuration);
        examFileGenerator.generate();

        var questionsFileGenerator = new QuestionsFileGenerator(configuration);
        questionsFileGenerator.setTags(examFileGenerator.getAllTags());
        questionsFileGenerator.generate();

        var report = new GeneratorReport(examFileGenerator, questionsFileGenerator, configuration);
        report.print();
    }

}
