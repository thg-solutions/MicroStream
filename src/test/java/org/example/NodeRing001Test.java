package org.example;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.example.pojos.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import java.nio.file.Path;
import java.time.Instant;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static org.example.StorageEngineUtils.infoToCleanFolder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeRing001Test {

    @Test
    void test001(TestInfo info, TestReporter reporter) {
        final Path tempFolder = infoToCleanFolder().apply(info);
        final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);

        final Node node01 = new Node(1);
        final Node node02 = new Node(2);
        final Node node03 = new Node(3);
        final Node node04 = new Node(4);

        node01.setLeft(node02);
        node02.setLeft(node03);
        node03.setLeft(node04);

        node04.setLeft(node01);

        final Instant start = now();
        storageManagerA.setRoot(node01);
        storageManagerA.storeRoot();
        final Instant stop = now();
        reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());
        storageManagerA.shutdown();

        final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
        final Node node01Again = (Node) storageManagerB.root();

        final Node node02Again = node01Again.getLeft();
        final Node node03Again = node02Again.getLeft();
        final Node node04Again = node03Again.getLeft();

        assertEquals(1, node01Again.getId());
        assertEquals(2, node02Again.getId());
        assertEquals(3, node03Again.getId());
        assertEquals(4, node04Again.getId());

        assertEquals(1, node04Again.getLeft().getId());

        storageManagerB.shutdown();
    }
}
