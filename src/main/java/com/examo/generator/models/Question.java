package com.examo.generator.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class Question {

    Set<String> tags;
    String question;
    List<Answer> answers;
    String hint;

    @Override
    public String toString() {
        return """
                %s
                %s:://
                %s
                ???
                %s
                """
                .formatted(String.join("::", tags),
                           question,
                           answers.stream().map(Answer::toString).collect(Collectors.joining("\n")),
                           hint);
    }

    @Setter
    public static class Answer {

        String text;
        float scores = 0f;

        @Override
        public String toString() {
            return """
                    %s@%.1f"""
                    .formatted(text, scores)
                    .replaceAll(",", ".");
        }

    }

}
