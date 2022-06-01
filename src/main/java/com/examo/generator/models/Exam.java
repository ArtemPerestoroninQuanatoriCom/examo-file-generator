package com.examo.generator.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class Exam {

    String title;
    List<Template> templates = new ArrayList<>();
    Duration duration;

    @Override
    public String toString() {

        long seconds = duration.getSeconds();
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        String timeInHHMMSS = String.format("%02d:%02d:%02d", HH, MM, SS);
        return """
                Title: %s
                %s
                Duration: %s
                """
                .formatted(title,
                           templates.stream()
                                   .map(Template::toString)
                                   .collect(Collectors.joining("\n")),
                           timeInHHMMSS);
    }

    @AllArgsConstructor
    @Getter
    public static class Template {

        Set<String> tags;
        int questionNumbers;

        @Override
        public String toString() {
            return "Template: %s - %d"
                    .formatted(String.join("::", tags), questionNumbers);
        }

    }

}
