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
        ExamFileGenerator examFileGenerator = new ExamFileGenerator(configuration);
        examFileGenerator.generate();

        QuestionsFileGenerator questionsFileGenerator = new QuestionsFileGenerator(configuration);
        Set<String> tags = examFileGenerator.getAllTags();

        questionsFileGenerator.setTags(new ArrayList<>(tags));
        questionsFileGenerator.generate();

        GeneratorReport report = new GeneratorReport(examFileGenerator, questionsFileGenerator);
        report.print();
    }

}
