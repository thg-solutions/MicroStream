package org.example;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.example.pojos.CollectionRootNode;
import org.example.pojos.HelloWorldImmutable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

import static java.time.Duration.between;
import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;
import static org.example.StorageEngineUtils.infoToCleanFolder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListOfElementsTest {

    @Test
    void test001(TestInfo info, TestReporter reporter) {
        final Path tempFolder = infoToCleanFolder().apply(info);
        final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);

        List<HelloWorldImmutable> elements = IntStream.range(0, 100_000)
                .mapToObj(i -> new HelloWorldImmutable(i + ""))
                .collect(toList());

        final Instant start = now();
        storageManagerA.setRoot(new CollectionRootNode(elements));
        storageManagerA.storeRoot();
        final Instant stop = now();
        reporter.publishEntry("duration store [ms] " + between(start, stop).toMillis());

        storageManagerA.shutdown();

        final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
        final CollectionRootNode rootAgain = (CollectionRootNode) storageManagerB.root();

        assertEquals(elements, rootAgain.getElements());

        storageManagerB.shutdown();
    }
}
