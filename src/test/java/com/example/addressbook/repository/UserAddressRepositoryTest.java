package com.example.addressbook.repository;

import com.example.addressbook.entity.UserAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository layer tests using in-memory database.
 */
@DataJpaTest
class UserAddressRepositoryTest {

    @Autowired
    private UserAddressRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should save and retrieve address")
    void shouldSaveAndRetrieve() {
        UserAddress address = UserAddress.builder()
                .name("Repository Test User")
                .email("repo@test.com")
                .city("Boston")
                .build();

        UserAddress saved = repository.save(address);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("Should search by keyword")
    void shouldSearchByKeyword() {
        repository.save(UserAddress.builder()
                .name("John Smith")
                .email("john@test.com")
                .city("New York")
                .build());
        repository.save(UserAddress.builder()
                .name("Jane Doe")
                .email("jane@test.com")
                .city("Los Angeles")
                .build());

        List<UserAddress> results = repository.searchByKeyword("John");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("John Smith");

        results = repository.searchByKeyword("York");
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should find by name containing")
    void shouldFindByNameContaining() {
        repository.save(UserAddress.builder()
                .name("Alice Johnson")
                .build());
        repository.save(UserAddress.builder()
                .name("Bob Williams")
                .build());

        List<UserAddress> results = repository.findByNameContainingIgnoreCase("alice");
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Alice Johnson");
    }

    @Test
    @DisplayName("Should find by city ignore case")
    void shouldFindByCityIgnoreCase() {
        repository.save(UserAddress.builder()
                .name("User 1")
                .city("Seattle")
                .build());
        repository.save(UserAddress.builder()
                .name("User 2")
                .city("seattle")
                .build());

        List<UserAddress> results = repository.findByCityIgnoreCase("SEATTLE");
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Should find by email ignore case")
    void shouldFindByEmailIgnoreCase() {
        repository.save(UserAddress.builder()
                .name("Email Test")
                .email("Test@Example.com")
                .build());

        List<UserAddress> results = repository.findByEmailIgnoreCase("test@example.com");
        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should handle timestamps on persist")
    void shouldHandleTimestamps() {
        UserAddress address = UserAddress.builder()
                .name("Timestamp Test")
                .build();

        UserAddress saved = repository.save(address);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
}
