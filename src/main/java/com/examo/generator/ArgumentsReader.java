package com.examo.generator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

import static com.examo.generator.GlobalParameters.examsCount;
import static com.examo.generator.GlobalParameters.maxQuestionsInGroup;
import static com.examo.generator.GlobalParameters.minQuestionsInGroup;
import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.joining;

@Slf4j
public class ArgumentsReader {

    public static final String EXAMS_COUNT = "e";
    public static final String MIN_QUESTION_IN_GROUP = "minQG";
    public static final String MAX_QUESTION_IN_GROUP = "maxQG";
    public static final String MIN_QUESTIONS_FOR_TEMPLATE = "maxQT";
    public static final String MAX_QUESTIONS_FOR_TEMPLATE = "maxQT";
    public static final String MIN_ANSWERS_COUNT = "maxAC";

    public void readArgs(String[] args) {
        CommandLine cmd = getCommandLine(args);

        log.debug("Run parameters: {}", Arrays.stream(cmd.getOptions())
                .map(option -> option.getLongOpt() + " = " + option.getValue())
                .collect(joining("; ")));

        if (cmd.hasOption(EXAMS_COUNT)) {
            examsCount = parseInt(cmd.getOptionValue(EXAMS_COUNT));
        }

        if (cmd.hasOption(MIN_QUESTION_IN_GROUP)) {
            minQuestionsInGroup = parseInt(cmd.getOptionValue(MIN_QUESTION_IN_GROUP));
        }

        if (cmd.hasOption(MAX_QUESTION_IN_GROUP)) {
            maxQuestionsInGroup = parseInt(cmd.getOptionValue(MAX_QUESTION_IN_GROUP));
        }

    }

    private CommandLine getCommandLine(String[] args) {
        Options options = getOptions();
        try {
            return new BasicParser().parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("codegen", options);
            throw new RuntimeException(pe);
        }
    }

    private Options getOptions() {
        Options options = new Options();

        Option examsCountOption = new Option(EXAMS_COUNT, "examsCount", true, "exams count");
        examsCountOption.setRequired(false);
        options.addOption(examsCountOption);

        Option minQuestionsInGroupOption = new Option(MIN_QUESTION_IN_GROUP, "minQuestionsInGroup", true, "min number of questions that will be generated for one group");
        minQuestionsInGroupOption.setRequired(false);
        options.addOption(minQuestionsInGroupOption);

        Option maxQuestionsInGroupOption = new Option(MAX_QUESTION_IN_GROUP, "maxQuestionsInGroup", true, "min number of questions that will be generated for one group");
        maxQuestionsInGroupOption.setRequired(false);
        options.addOption(maxQuestionsInGroupOption);

        Option minQuestionsForTemplateOption = new Option(MIN_QUESTIONS_FOR_TEMPLATE, "minQuestionsForTemplate", true, "min number of questions that will be generated for one group");
        minQuestionsForTemplateOption.setRequired(false);
        options.addOption(minQuestionsForTemplateOption);

        Option maxQuestionsForTemplateOption = new Option(MAX_QUESTIONS_FOR_TEMPLATE, "maxQuestionsForTemplate", true, "min number of questions that will be generated for one group");
        maxQuestionsForTemplateOption.setRequired(false);
        options.addOption(maxQuestionsForTemplateOption);

        Option minAnswersCountOption = new Option(MIN_ANSWERS_COUNT, "minAnswersCount", true, "min number of questions that will be generated for one group");
        minAnswersCountOption.setRequired(false);
        options.addOption(minAnswersCountOption);

        return options;
    }

}
