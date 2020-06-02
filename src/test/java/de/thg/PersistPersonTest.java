package de.thg;

import de.thg.persistence.PersonRepository;
import de.thg.pojos.Address;
import de.thg.pojos.People;
import de.thg.pojos.Person;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.StorageEngineUtils.recreateTempFolder;

public class PersistPersonTest {

    private static Path TARGET_PATH = new File("./build/storage/").toPath();

    private static PersonRepository personRepository;

    @BeforeEach
    void setup() {
        recreateTempFolder(TARGET_PATH);
        People people = new People();
        people.addPerson(new Person("James", "Bond", new Address("street", "city", "country")));
        people.addPerson((new Person("Jason", "Bourne")));
        people.addPerson(new Person("Jack", "Bauer"));
        EmbeddedStorageManager storageManager = EmbeddedStorage.start(people, TARGET_PATH);
        storageManager.storeRoot();
        storageManager.shutdown();

        personRepository = new PersonRepository(EmbeddedStorage.start(TARGET_PATH));
    }

    @AfterEach
    void teardown() {
        personRepository.getStorageManager().shutdown();
    }


    @Test
    void findPersonByLastname() {
        List<Person> personList = personRepository.findByLastname("Bond");
        assertThat(personList).hasSize(1);
        assertThat(personList.get(0).getAddress()).isNotNull();
    }

    @Test
    void findPersonByFirstname() {
        List<Person> personList = personRepository.findByFirstname("Jason");
        assertThat(personList).hasSize(1);
        assertThat(personList.get(0).getAddress()).isNull();
    }

    @Test
    void findPersonByLastnameStartingWith() {
        List<Person> personList = personRepository.findByLastnameStartingWith("Bo");
        assertThat(personList).hasSize(2);
    }

    @Test
    void deletePersonFromList() {
        List<Person> personList = personRepository.findAll();
        personList.removeAll(personRepository.findByFirstname("Jason"));
        personRepository.update(personList);
        personRepository.getStorageManager().shutdown();
        personRepository = new PersonRepository(EmbeddedStorage.start(TARGET_PATH));
        assertThat(personRepository.findAll()).hasSize(2);
    }

    @Test
    void addPerson() {
        List<Person> personList = personRepository.findAll();
        personList.add(new Person("Jack", "Reacher"));
        personRepository.update(personList);
        personRepository.getStorageManager().shutdown();
        personRepository = new PersonRepository(EmbeddedStorage.start(TARGET_PATH));
        assertThat(personRepository.findByLastnameStartingWith("R")).hasSize(1);
        assertThat(personRepository.findByLastnameStartingWith("B")).hasSize(3);
    }

    @Test
    void updateSinglePerson() {
        Address address = personRepository.findByLastname("Bond").get(0).getAddress();
        address.setCity("Chelsea");
        personRepository.update(address);
        personRepository.getStorageManager().shutdown();
        personRepository = new PersonRepository(EmbeddedStorage.start(TARGET_PATH));
        assertThat(personRepository.findByFirstname("James").get(0).getAddress().getCity()).isEqualTo("Chelsea");
    }
}
