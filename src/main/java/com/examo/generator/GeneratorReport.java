package com.examo.generator;

import com.examo.generator.models.Exam;
import com.examo.generator.models.Question;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.repeat;

@Slf4j
public class GeneratorReport {

    private final ExamFileGenerator examFileGenerator;
    private final QuestionsFileGenerator questionsFileGenerator;

    public GeneratorReport(ExamFileGenerator examFileGenerator, QuestionsFileGenerator questionsFileGenerator) {
        this.examFileGenerator = examFileGenerator;
        this.questionsFileGenerator = questionsFileGenerator;
    }

    public void print() {
        List<Exam> exams = examFileGenerator.getExams();
        List<Question> questions = questionsFileGenerator.getQuestions();

        newBlock();
        log.info("Generated {} tags:", examFileGenerator.getAllTags().size());
        log.info("{}", examFileGenerator.getAllTags());
        Map<String, List<Exam.Template>> examsToTemplates = exams.stream().collect(toMap(
                Exam::getTitle,
                Exam::getTemplates));

        newBlock();
        Map<Exam.Template, Set<Question>> templatesToQuestionsMapping = new HashMap<>();
        Set<Exam.Template> allTemplates = examsToTemplates.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        questions.forEach(question -> allTemplates.forEach(template -> {
            if (question.getTags().containsAll(template.getTags())) {
                templatesToQuestionsMapping.putIfAbsent(template, new HashSet<>());
                Set<Question> questionSet = templatesToQuestionsMapping.get(template);
                questionSet.add(question);
            }
        }));
        log.info("Generated {} questions for templates:", questions.size());
        templatesToQuestionsMapping.forEach(
                (template, generatedQuestions) -> log.info("{}:{}", template.getTags(), generatedQuestions.size())
        );

        newBlock();
        log.info("Stats by tag:");
        Map<String, Integer> byTagMap = new ConcurrentHashMap<>();
        for (Question question : questions) {
            question.getTags()
                    .forEach(tag -> byTagMap.put(tag, byTagMap.getOrDefault(tag, 0) + 1));
        }
        byTagMap.forEach((tag, count) -> log.info("{}:{}", tag, count));
        newBlock();
        log.info("Generated {} exams with tags and suitable questions number:", exams.size());
        examsToTemplates.forEach((title, templates) -> log.info("{}: {} : {}", title,
                                                                templates.stream()
                                                                        .map(Exam.Template::getTags)
                                                                        .collect(Collectors.toList()),
                                                                templates.stream()
                                                                        .map(t -> templatesToQuestionsMapping.getOrDefault(t, new HashSet<>()).size())
                                                                        .collect(summarizingInt(Integer::intValue))));
        newBlock();
        templatesToQuestionsMapping.forEach((template, q) -> {
            if (template.getQuestionNumbers() > q.size()) {
                log.error("Template {} has no enough questions. available only {}", template, q.size());
            }
        });

    }

    private void newBlock() {
        log.info(repeat("~", 80));
    }

}
