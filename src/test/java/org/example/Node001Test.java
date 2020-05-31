package org.example;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.example.pojos.BinaryTree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Random;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static org.example.StorageEngineUtils.infoToCleanFolder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Node001Test {

    @Test
    void test001(TestInfo info, TestReporter reporter) {
        final Path tempFolder = infoToCleanFolder().apply(info);
        final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);

        final BinaryTree tree = new BinaryTree();
        new Random().ints()
                .limit(1_000)
                .forEach(tree::add);

        final Instant start = now();
        storageManagerA.setRoot(tree);
        storageManagerA.storeRoot();
        final Instant stop = now();
        reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());
        storageManagerA.shutdown();

        final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
        final BinaryTree rootAgain = (BinaryTree) storageManagerB.root();
        assertEquals(tree.traverseLevelOrder(), rootAgain.traverseLevelOrder());
        storageManagerB.shutdown();
    }
}
