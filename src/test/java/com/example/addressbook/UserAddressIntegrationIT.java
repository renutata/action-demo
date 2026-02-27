package com.example.addressbook;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.entity.UserAddress;
import com.example.addressbook.repository.UserAddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the User Address API.
 * Tests the entire flow from controller to database.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserAddressIntegrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAddressRepository repository;

    private static Long createdAddressId;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        repository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Integration: Create address successfully")
    void shouldCreateAddressSuccessfully() throws Exception {
        UserAddressDTO dto = UserAddressDTO.builder()
                .name("Integration Test User")
                .phone("9876543210")
                .email("integration@test.com")
                .street("456 Test Ave")
                .city("San Francisco")
                .state("CA")
                .zipCode("94102")
                .country("USA")
                .build();

        MvcResult result = mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Integration Test User")))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andReturn();

        // Store the created ID for subsequent tests
        UserAddressDTO created = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserAddressDTO.class);
        createdAddressId = created.getId();

        // Verify in database
        assertThat(repository.findById(createdAddressId)).isPresent();
    }

    @Test
    @Order(2)
    @DisplayName("Integration: Get all addresses")
    void shouldGetAllAddresses() throws Exception {
        // Create test data
        repository.save(UserAddress.builder()
                .name("User 1")
                .email("user1@test.com")
                .build());
        repository.save(UserAddress.builder()
                .name("User 2")
                .email("user2@test.com")
                .build());

        mockMvc.perform(get("/api/addresses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(3)
    @DisplayName("Integration: Get address by ID")
    void shouldGetAddressById() throws Exception {
        UserAddress saved = repository.save(UserAddress.builder()
                .name("Find Me User")
                .email("findme@test.com")
                .city("Boston")
                .build());

        mockMvc.perform(get("/api/addresses/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Find Me User")))
                .andExpect(jsonPath("$.city", is("Boston")));
    }

    @Test
    @Order(4)
    @DisplayName("Integration: Update address")
    void shouldUpdateAddress() throws Exception {
        UserAddress saved = repository.save(UserAddress.builder()
                .name("Original Name")
                .email("original@test.com")
                .build());

        UserAddressDTO updateDTO = UserAddressDTO.builder()
                .name("Updated Name")
                .email("updated@test.com")
                .city("Updated City")
                .build();

        mockMvc.perform(put("/api/addresses/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.email", is("updated@test.com")))
                .andExpect(jsonPath("$.city", is("Updated City")));

        // Verify in database
        UserAddress updated = repository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Test
    @Order(5)
    @DisplayName("Integration: Delete address")
    void shouldDeleteAddress() throws Exception {
        UserAddress saved = repository.save(UserAddress.builder()
                .name("Delete Me")
                .email("delete@test.com")
                .build());

        mockMvc.perform(delete("/api/addresses/" + saved.getId()))
                .andExpect(status().isNoContent());

        // Verify deleted from database
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    @Order(6)
    @DisplayName("Integration: Search addresses by keyword")
    void shouldSearchAddresses() throws Exception {
        repository.save(UserAddress.builder()
                .name("John Smith")
                .email("john@test.com")
                .city("Chicago")
                .build());
        repository.save(UserAddress.builder()
                .name("Jane Doe")
                .email("jane@test.com")
                .city("New York")
                .build());

        // Search by name
        mockMvc.perform(get("/api/addresses/search")
                .param("q", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Smith")));

        // Search by city
        mockMvc.perform(get("/api/addresses/search")
                .param("q", "Chicago"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(7)
    @DisplayName("Integration: Search by name endpoint")
    void shouldSearchByName() throws Exception {
        repository.save(UserAddress.builder()
                .name("Alice Johnson")
                .email("alice@test.com")
                .build());

        mockMvc.perform(get("/api/addresses/search/name")
                .param("name", "Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", containsString("Alice")));
    }

    @Test
    @Order(8)
    @DisplayName("Integration: Search by city endpoint")
    void shouldSearchByCity() throws Exception {
        repository.save(UserAddress.builder()
                .name("Bob Wilson")
                .city("Seattle")
                .build());

        mockMvc.perform(get("/api/addresses/search/city")
                .param("city", "Seattle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Order(9)
    @DisplayName("Integration: Return 404 for non-existent address")
    void shouldReturn404ForNonExistent() throws Exception {
        mockMvc.perform(get("/api/addresses/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("99999")));
    }

    @Test
    @Order(10)
    @DisplayName("Integration: Validation error for invalid input")
    void shouldReturnValidationError() throws Exception {
        UserAddressDTO invalidDTO = UserAddressDTO.builder()
                .name("") // Empty name - should fail validation
                .email("not-an-email")
                .build();

        mockMvc.perform(post("/api/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", notNullValue()));
    }
}
