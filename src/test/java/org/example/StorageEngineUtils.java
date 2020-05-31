package org.example;

import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.function.Function;

import static java.nio.file.Files.walk;
import static java.util.Comparator.reverseOrder;

public interface StorageEngineUtils {

    String TARGET_PATH = "./build/storage/";

    static Function<TestInfo, Path> infoToCleanFolder() {
        return (info) -> {
            final Class<?> aClass = info.getTestClass()
                    .get();
            final Method method = info.getTestMethod()
                    .get();
            final Path tempFolder = new File(TARGET_PATH, aClass.getSimpleName() + "_" + method.getName()).toPath();
            recreateTempFolder(tempFolder);
            return tempFolder;
        };
    }

    static void recreateTempFolder(Path tempFolder) {
        if (tempFolder.toFile().exists()) {
            try {
                walk(tempFolder).sorted(reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tempFolder.toFile().mkdirs();
    }

    static Function<TestInfo, Path> infoToCleanExportFolder(String postFix) {
        return (info) -> {
            final Class<?> aClass = info.getTestClass()
                    .get();
            final Method method = info.getTestMethod()
                    .get();
            final Path tempFolder = new File(TARGET_PATH, aClass.getSimpleName() + "_" + method.getName() + "_" + postFix).toPath();
            recreateTempFolder(tempFolder);
            return tempFolder;
        };
    }

}
