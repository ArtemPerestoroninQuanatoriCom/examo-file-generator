package com.examo.generator.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.stream.Collectors.joining;

@Slf4j
public class ArgumentsReader {

    private final Configuration configuration = new Configuration();
    private Map<Option, TypedConsumer<?>> configMap;

    public Configuration readArgs(String[] args) {
        CommandLine cmd = getCommandLine(args);
        log.debug("Run parameters: {}", Arrays.stream(cmd.getOptions())
                .map(option -> option.getLongOpt() + " = " + option.getValue())
                .collect(joining("; ")));

        getConfigurationMapping().forEach((key, value) -> setConfigValue(cmd, key, value));
        return configuration;
    }

    /**
     * mapping expected options on setters of configuration values.
     *
     * @return option -> value consumer mapping
     */
    private Map<Option, TypedConsumer<?>> getConfigurationMapping() {
        if (configMap == null) {
            configMap = new HashMap<>();
            Option examCountOption = new Option("e", "examsCount", true, "exams count. Default=" + configuration.getExamsCount());
            Option minQuestionsInGroupOption = new Option("minQG", "minQuestionsInGroup", true, "Min number of questions that will be generated for one group. Default=" + configuration.getMinQuestionsInGroup());
            Option maxQuestionsInGroupOption = new Option("maxQG", "maxQuestionsInGroup", true, "Max number of questions that will be generated for one group. Default=" + configuration.getMaxQuestionsInGroup());
            Option minQuestionsForTemplateOption = new Option("minQT", "minQuestionsForTemplate", true, "Min number of questions that will be generated for one template. Default=" + configuration.getMinQuestionsForTemplate());
            Option maxQuestionsForTemplateOption = new Option("maxQT", "maxQuestionsForTemplate", true, "Max number of questions that will be generated for one template. Default=" + configuration.getMaxQuestionsForTemplate());
            Option minAnswersCountOption = new Option("minAC", "minAnswersCount", true, "Min number of answers that will be generated for question. Default=" + configuration.getMinAnswersCount());
            Option maxAnswersCountOption = new Option("maxAC", "maxAnswersCount", true, "Max number of answers that will be generated for question. Default=" + configuration.getMaxAnswersCount());
            Option outputDirOption = new Option("out", "outputDir", true, "Directory to generate files. Default=" + configuration.getOutputDir());
            Option maxTemplatesForExamOption = new Option("maxTE", "maxTemplatesForExam", true, "Max number of templates that will be generated for exam. Default=" + configuration.getMaxTemplatesForExam());
            Option maxTagsForTemplateOption = new Option("maxTT", "maxTagsForTemplate", true, "Max number of tags that will be included in template. Default=" + configuration.getMaxAnswersCount());
            Option maxTagsForQuestionOption = new Option("maxTQ", "maxTagsForQuestion", true, "Max number of tags that will be included in question. Default=" + configuration.getMaxTagsForQuestion());

            configMap.put(examCountOption, new TypedConsumer<>(Integer.class, configuration::setExamsCount));
            configMap.put(minQuestionsInGroupOption, new TypedConsumer<>(Integer.class, configuration::setMaxQuestionsInGroup));
            configMap.put(maxQuestionsInGroupOption, new TypedConsumer<>(Integer.class, configuration::setMaxQuestionsInGroup));
            configMap.put(minQuestionsForTemplateOption, new TypedConsumer<>(Integer.class, configuration::setMinQuestionsForTemplate));
            configMap.put(maxQuestionsForTemplateOption, new TypedConsumer<>(Integer.class, configuration::setMaxQuestionsForTemplate));
            configMap.put(minAnswersCountOption, new TypedConsumer<>(Integer.class, configuration::setMinAnswersCount));
            configMap.put(maxAnswersCountOption, new TypedConsumer<>(Integer.class, configuration::setMaxAnswersCount));
            configMap.put(outputDirOption, new TypedConsumer<>(String.class, configuration::setOutputDir));
            configMap.put(maxTemplatesForExamOption, new TypedConsumer<>(Integer.class, configuration::setMaxTemplatesForExam));
            configMap.put(maxTagsForTemplateOption, new TypedConsumer<>(Integer.class, configuration::setMaxTagsForTemplate));
            configMap.put(maxTagsForQuestionOption, new TypedConsumer<>(Integer.class, configuration::setMaxTagsForQuestion));
        }

        return configMap;
    }


    @SuppressWarnings("unchecked")
    private <T> void setConfigValue(CommandLine cmd, Option option, TypedConsumer<T> typedConsumer) {
        if (!cmd.hasOption(option.getOpt())) {
            return;
        }
        String value = cmd.getOptionValue(option.getOpt());

        TypedSupplier<T> supplier = (TypedSupplier<T>) getHandlers(value).stream()
                .filter(handler -> handler.getSupplierType().equals(typedConsumer.getConsumerType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("can't handle type " + typedConsumer.consumerType + " for configuration field associated with" + option.getOpt()));

        typedConsumer.getConsumer().accept(supplier.getSupplier().get());
    }

    /**
     * Handlers that convert argument value that has read as String into expected type;
     *
     * @param value argument value;
     * @return list of converters into different possible types;
     */
    private List<TypedSupplier<?>> getHandlers(String value) {
        List<TypedSupplier<?>> handlers = new ArrayList<>();
        handlers.add(new TypedSupplier<>(Integer.class, () -> Integer.valueOf(value)));
        handlers.add(new TypedSupplier<>(String.class, () -> value));
        handlers.add(new TypedSupplier<>(Boolean.class, () -> Boolean.valueOf(value)));
        return handlers;
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
        getConfigurationMapping().keySet().forEach(options::addOption);
        return options;
    }

}

@AllArgsConstructor
@Getter
class TypedConsumer<T> {

    Class<T> consumerType;
    Consumer<T> consumer;

}

@AllArgsConstructor
@Getter
class TypedSupplier<T> {

    Class<T> supplierType;
    Supplier<T> supplier;

}
