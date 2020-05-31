package org.example;

import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.example.pojos.HelloWorld;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;

import static org.example.StorageEngineUtils.infoToCleanFolder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloWorldTest {

    @Test
    void test001(TestInfo info) {
        final HelloWorld value = new HelloWorld();
        value.setValue("HelloWorld");

        final Path tempfolder = infoToCleanFolder().apply(info);

        final EmbeddedStorageManager storageManagerA = EmbeddedStorage.start(tempfolder);
        storageManagerA.setRoot(value);
        storageManagerA.storeRoot();
        storageManagerA.shutdown();

        final EmbeddedStorageManager storageManagerB = EmbeddedStorage.start(tempfolder);
        final Object root = storageManagerB.root();
        assertTrue(root instanceof HelloWorld);
        HelloWorld helloWorld = (HelloWorld) root;
        assertEquals("HelloWorld", helloWorld.getValue());
        storageManagerB.shutdown();
    }
}