package com.examo.generator;

import com.examo.generator.config.Configuration;
import com.examo.generator.models.Exam;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.RandomUtils.nextLong;

@Slf4j
public class ExamFileGenerator extends FileGenerator {

    public static final String OUTPUT_FILE = "exam.txt";
    public static final String SEPARATOR = """
            ~~~
            """;
    @Getter
    private final List<Exam> exams = new ArrayList<>();

    public ExamFileGenerator(Configuration configuration) {
        super(configuration);

    }

    public void generate() throws IOException {
        File outputFile = createFile(OUTPUT_FILE);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            addHeaders(outputStream);
            addExams(outputStream);
        }
    }

    public Set<String> getAllTags() {
        return exams.stream()
                .map(Exam::getTemplates)
                .flatMap(templates -> templates.stream().flatMap(template -> template.getTags().stream()))
                .collect(Collectors.toSet());
    }

    private void addHeaders(FileOutputStream outputStream) throws IOException {
        outputStream.write("Type: Exam\n".getBytes(UTF_8));
        outputStream.flush();
    }

    private void addExams(FileOutputStream outputStream) throws IOException {
        for (int i = 0; i < configuration.getExamsCount(); i++) {
            Exam exam = generateExam();
            exams.add(exam);
            outputStream.write(exam.toString().getBytes(UTF_8));
            outputStream.write(SEPARATOR.getBytes(UTF_8));
            outputStream.flush();
        }
    }

    private Exam generateExam() {
        Exam exam = new Exam();

        exam.setTitle("%s %s".formatted(FAKER.hacker().adjective(), FAKER.programmingLanguage().name()));

        for (int i = 0; i < new Random().nextInt(2) + 1; i++) {
            Set<String> tags = new HashSet<>();
            for (int j = 0; j < new Random().nextInt(2) + 1; j++) {
                tags.add(FAKER.app().name());
            }
            exam.getTemplates().add(new Exam.Template(tags, nextInt(configuration.getMinQuestionsForTemplate(), configuration.getMaxQuestionsForTemplate() + 1)));
        }

        exam.setDuration(Duration.ofSeconds(nextLong(1800, 14400)));

        return exam;
    }

}
