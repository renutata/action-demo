package com.example.addressbook.controller;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.exception.GlobalExceptionHandler;
import com.example.addressbook.exception.ResourceNotFoundException;
import com.example.addressbook.service.UserAddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for UserAddressController.
 */
@WebMvcTest(UserAddressController.class)
@Import(GlobalExceptionHandler.class)
class UserAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserAddressService service;

    private UserAddressDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = UserAddressDTO.builder()
                .id(1L)
                .name("John Doe")
                .phone("1234567890")
                .email("john@example.com")
                .street("123 Main St")
                .city("New York")
                .state("NY")
                .zipCode("10001")
                .country("USA")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    @DisplayName("POST /api/addresses")
    class CreateEndpointTests {

        @Test
        @DisplayName("Should create address and return 201")
        void shouldCreateAddress() throws Exception {
            when(service.create(any(UserAddressDTO.class))).thenReturn(testDTO);

            mockMvc.perform(post("/api/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("John Doe")))
                    .andExpect(jsonPath("$.email", is("john@example.com")));

            verify(service, times(1)).create(any(UserAddressDTO.class));
        }

        @Test
        @DisplayName("Should return 400 for invalid input")
        void shouldReturn400ForInvalidInput() throws Exception {
            UserAddressDTO invalidDTO = UserAddressDTO.builder()
                    .name("") // Name is required
                    .email("invalid-email")
                    .build();

            mockMvc.perform(post("/api/addresses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/addresses/{id}")
    class GetByIdEndpointTests {

        @Test
        @DisplayName("Should return address when found")
        void shouldReturnAddressWhenFound() throws Exception {
            when(service.getById(1L)).thenReturn(testDTO);

            mockMvc.perform(get("/api/addresses/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.name", is("John Doe")));
        }

        @Test
        @DisplayName("Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(service.getById(999L)).thenThrow(new ResourceNotFoundException("UserAddress", 999L));

            mockMvc.perform(get("/api/addresses/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsString("999")));
        }
    }

    @Nested
    @DisplayName("GET /api/addresses")
    class GetAllEndpointTests {

        @Test
        @DisplayName("Should return all addresses")
        void shouldReturnAllAddresses() throws Exception {
            UserAddressDTO anotherDTO = UserAddressDTO.builder()
                    .id(2L)
                    .name("Jane Doe")
                    .build();

            when(service.getAll()).thenReturn(Arrays.asList(testDTO, anotherDTO));

            mockMvc.perform(get("/api/addresses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name", is("John Doe")))
                    .andExpect(jsonPath("$[1].name", is("Jane Doe")));
        }

        @Test
        @DisplayName("Should return empty list when no addresses")
        void shouldReturnEmptyList() throws Exception {
            when(service.getAll()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/addresses"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("PUT /api/addresses/{id}")
    class UpdateEndpointTests {

        @Test
        @DisplayName("Should update address successfully")
        void shouldUpdateAddress() throws Exception {
            UserAddressDTO updatedDTO = UserAddressDTO.builder()
                    .id(1L)
                    .name("John Updated")
                    .email("updated@example.com")
                    .build();

            when(service.update(eq(1L), any(UserAddressDTO.class))).thenReturn(updatedDTO);

            mockMvc.perform(put("/api/addresses/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("John Updated")));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent")
        void shouldReturn404WhenUpdatingNonExistent() throws Exception {
            when(service.update(eq(999L), any(UserAddressDTO.class)))
                    .thenThrow(new ResourceNotFoundException("UserAddress", 999L));

            mockMvc.perform(put("/api/addresses/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/addresses/{id}")
    class DeleteEndpointTests {

        @Test
        @DisplayName("Should delete address and return 204")
        void shouldDeleteAddress() throws Exception {
            doNothing().when(service).delete(1L);

            mockMvc.perform(delete("/api/addresses/1"))
                    .andExpect(status().isNoContent());

            verify(service, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Should return 404 when deleting non-existent")
        void shouldReturn404WhenDeletingNonExistent() throws Exception {
            doThrow(new ResourceNotFoundException("UserAddress", 999L)).when(service).delete(999L);

            mockMvc.perform(delete("/api/addresses/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/addresses/search")
    class SearchEndpointTests {

        @Test
        @DisplayName("Should search addresses by keyword")
        void shouldSearchByKeyword() throws Exception {
            when(service.search("John")).thenReturn(Arrays.asList(testDTO));

            mockMvc.perform(get("/api/addresses/search")
                    .param("q", "John"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is("John Doe")));
        }

        @Test
        @DisplayName("Should search addresses by name")
        void shouldSearchByName() throws Exception {
            when(service.findByName("John")).thenReturn(Arrays.asList(testDTO));

            mockMvc.perform(get("/api/addresses/search/name")
                    .param("name", "John"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }

        @Test
        @DisplayName("Should search addresses by city")
        void shouldSearchByCity() throws Exception {
            when(service.findByCity("New York")).thenReturn(Arrays.asList(testDTO));

            mockMvc.perform(get("/api/addresses/search/city")
                    .param("city", "New York"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)));
        }
    }
}
