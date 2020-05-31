package org.example;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.example.pojos.HelloWorldImmutable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;

import static org.example.StorageEngineUtils.infoToCleanFolder;
import static org.junit.jupiter.api.Assertions.*;

public class HelloWorldImmutableTest {

    @Test
    void test001(TestInfo info) {
        final HelloWorldImmutable value = new HelloWorldImmutable("HelloWorldImmutable");

        final Path tempFolder = infoToCleanFolder().apply(info);
        final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempFolder);
        storageManagerA.setRoot(value);
        storageManagerA.storeRoot();
        storageManagerA.shutdown();

        final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempFolder);
        final Object root = storageManagerB.root();
        assertTrue(root instanceof HelloWorldImmutable);
        HelloWorldImmutable helloWorld = (HelloWorldImmutable) root;
        assertEquals("HelloWorldImmutable", helloWorld.getValue());
        storageManagerB.shutdown();
    }

}
