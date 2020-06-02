package de.thg.persistence;

import de.thg.pojos.People;
import de.thg.pojos.Person;
import one.microstream.storage.types.EmbeddedStorageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonRepository {

    private EmbeddedStorageManager storageManager;

    public PersonRepository() {
    }

    public PersonRepository(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public EmbeddedStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(EmbeddedStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public void update(Object o) {
        storageManager.store(o);
    }

    public void update(List<Person> personlist) {
        storageManager.store(personlist);
    }

    public List<Person> findAll() {
        if (storageManager.root() != null) {
            return ((People) storageManager.root()).getPeople();
        }
        return new ArrayList<>();
    }

    public List<Person> findByLastname(String lastname) {
        return findAll().stream()
                .filter(p -> p.getLastname().equals(lastname))
                .collect(Collectors.toList());
    }

    public List<Person> findByFirstname(String firstname) {
        return findAll().stream()
                .filter(p -> p.getFirstname().equals(firstname))
                .collect(Collectors.toList());
    }

    public List<Person> findByLastnameStartingWith(String abbr) {
        return findAll().stream()
                .filter(p -> p.getLastname().startsWith(abbr))
                .collect(Collectors.toList());
    }
}
