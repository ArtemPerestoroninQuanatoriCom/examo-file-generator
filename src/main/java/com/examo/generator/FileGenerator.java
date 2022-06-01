package com.examo.generator;

import com.github.javafaker.ExtendedFaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.examo.generator.GlobalParameters.OUTPUT_DIR;

public class FileGenerator {
    protected static final ExtendedFaker FAKER = ExtendedFaker.instance();

    protected File createFile(String fileName) throws IOException {
        File file = new File(OUTPUT_DIR.concat(fileName));
        if (file.exists()) {
            return file;
        }

        File directory = new File(OUTPUT_DIR);
        if (!directory.exists()) //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();

        try (FileWriter fw = new FileWriter(file.getAbsoluteFile());
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(file.getAbsolutePath());
        }
        return file;
    }

}
