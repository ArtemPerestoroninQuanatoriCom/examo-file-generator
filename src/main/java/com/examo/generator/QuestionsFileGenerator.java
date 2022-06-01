package com.examo.generator;

import com.examo.generator.models.Question;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.examo.generator.GlobalParameters.maxAnswersCount;
import static com.examo.generator.GlobalParameters.maxQuestionsInGroup;
import static com.examo.generator.GlobalParameters.minAnswersCount;
import static com.examo.generator.GlobalParameters.minQuestionsInGroup;
import static java.lang.Math.min;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class QuestionsFileGenerator extends FileGenerator {

    public static final String OUTPUT_FILE = "questions.txt";
    public static final String SEPARATOR = """
            ~~~
            """;
    @Getter
    private final List<Question> questions = new ArrayList<>();
    @Setter
    private ArrayList<String> tags;

    public void generate() throws IOException {
        File outputFile = createFile(OUTPUT_FILE);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            addHeaders(outputStream);
            for (String tag : tags) {
                for (int i = 0; i < nextInt(minQuestionsInGroup, maxQuestionsInGroup + 1); i++) {
                    Set<String> tags = getRandomTagsInclude(tag);
                    addQuestions(outputStream, tags);
                }
            }
        }
    }

    private void addHeaders(FileOutputStream outputStream) throws IOException {
        outputStream.write(
                """
                        Type: Questions
                        For: Exam
                        """.getBytes(UTF_8));
        outputStream.flush();
    }

    private void addQuestions(FileOutputStream outputStream, Set<String> tags) throws IOException {
        Question question = new Question();

        question.setTags(tags);

        question.setQuestion(getQuestion());

        List<Question.Answer> answers = new ArrayList<>();
        for (int i = 0; i < nextInt(minAnswersCount, maxAnswersCount + 1); i++) {
            Question.Answer answer = new Question.Answer();
            answer.setText(getAnswer());
            answers.add(answer);
        }
        answers.get(nextInt(0, answers.size())).setScores(1f);
        question.setAnswers(answers);

        question.setHint(FAKER.rickAndMorty().quote());

        questions.add(question);
        outputStream.write(question.toString().getBytes(UTF_8));
        outputStream.write(SEPARATOR.getBytes(UTF_8));
        outputStream.flush();
    }

    private String getAnswer() {
        return "%s %s"
                .formatted(FAKER.verbs().past(),
                           FAKER.book().title());
    }

    private String getQuestion() {
        return "What did the %s %s when %s said that %s?"
                .formatted(FAKER.animal().name(),
                           FAKER.verbs().base(),
                           FAKER.ancient().hero(),
                           FAKER.chuckNorris().fact());
    }

    private Set<String> getRandomTagsInclude(String tag) {
        int tagsCount = nextInt(1, min(4, tags.size()));
        Set<String> random = new HashSet<>();
        random.add(tag);
        while (tagsCount != random.size()) {
            int index = nextInt(0, tags.size());
            String next = tags.get(index);
            if (random.contains(next)) {
                continue;
            }
            random.add(next);
        }
        return random;
    }

}
