package com.examo.generator;

import com.examo.generator.config.Configuration;
import com.github.javafaker.ExtendedFaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileGenerator {

    protected static final ExtendedFaker FAKER = ExtendedFaker.instance();
    protected final Configuration configuration;

    public FileGenerator(Configuration configuration) {this.configuration = configuration;}

    protected File createFile(String fileName) throws IOException {
        File file = new File(configuration.getOutputDir().concat(fileName));
        if (file.exists()) {
            return file;
        }

        File directory = new File(configuration.getOutputDir());
        if (!directory.exists()) //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();

        try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(file.getAbsolutePath());
        }
        return file;
    }

}
