package com.example.addressbook.mapper;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.entity.UserAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for UserAddressMapper.
 */
class UserAddressMapperTest {

    private UserAddressMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserAddressMapper();
    }

    @Test
    @DisplayName("Should convert entity to DTO")
    void shouldConvertEntityToDTO() {
        UserAddress entity = UserAddress.builder()
                .id(1L)
                .name("Test User")
                .phone("1234567890")
                .email("test@example.com")
                .street("123 Test St")
                .city("Test City")
                .state("TS")
                .zipCode("12345")
                .country("Test Country")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserAddressDTO dto = mapper.toDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Test User");
        assertThat(dto.getPhone()).isEqualTo("1234567890");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getStreet()).isEqualTo("123 Test St");
        assertThat(dto.getCity()).isEqualTo("Test City");
        assertThat(dto.getState()).isEqualTo("TS");
        assertThat(dto.getZipCode()).isEqualTo("12345");
        assertThat(dto.getCountry()).isEqualTo("Test Country");
    }

    @Test
    @DisplayName("Should return null when converting null entity to DTO")
    void shouldReturnNullForNullEntity() {
        UserAddressDTO dto = mapper.toDTO(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Should convert DTO to entity")
    void shouldConvertDTOToEntity() {
        UserAddressDTO dto = UserAddressDTO.builder()
                .id(1L)
                .name("Test User")
                .phone("1234567890")
                .email("test@example.com")
                .street("123 Test St")
                .city("Test City")
                .state("TS")
                .zipCode("12345")
                .country("Test Country")
                .build();

        UserAddress entity = mapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Test User");
        assertThat(entity.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return null when converting null DTO to entity")
    void shouldReturnNullForNullDTO() {
        UserAddress entity = mapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    @DisplayName("Should update entity with DTO values")
    void shouldUpdateEntity() {
        UserAddress entity = UserAddress.builder()
                .id(1L)
                .name("Original Name")
                .email("original@example.com")
                .build();

        UserAddressDTO dto = UserAddressDTO.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .city("New City")
                .build();

        mapper.updateEntity(dto, entity);

        assertThat(entity.getName()).isEqualTo("Updated Name");
        assertThat(entity.getEmail()).isEqualTo("updated@example.com");
        assertThat(entity.getCity()).isEqualTo("New City");
        assertThat(entity.getId()).isEqualTo(1L); // ID should not change
    }

    @Test
    @DisplayName("Should not throw when updating with null values")
    void shouldHandleNullUpdateGracefully() {
        UserAddress entity = UserAddress.builder().name("Original").build();

        mapper.updateEntity(null, entity);
        assertThat(entity.getName()).isEqualTo("Original");

        mapper.updateEntity(UserAddressDTO.builder().build(), null);
        // Should not throw
    }
}
