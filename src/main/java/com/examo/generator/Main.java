package com.examo.generator;

import com.examo.generator.config.ArgumentsReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

@Slf4j
public class Main {

    public static void main(String[] args) throws IOException {
        var configuration = new ArgumentsReader().readArgs(args);
        var examFileGenerator = new ExamFileGenerator(configuration);
        examFileGenerator.generate();

        var questionsFileGenerator = new QuestionsFileGenerator(configuration);
        Set<String> tags = examFileGenerator.getAllTags();

        questionsFileGenerator.setTags(new ArrayList<>(tags));
        questionsFileGenerator.generate();

        var report = new GeneratorReport(examFileGenerator, questionsFileGenerator, configuration);
        report.print();
    }

}
