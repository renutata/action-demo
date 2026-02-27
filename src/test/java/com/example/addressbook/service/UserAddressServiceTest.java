package com.example.addressbook.service;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.entity.UserAddress;
import com.example.addressbook.exception.ResourceNotFoundException;
import com.example.addressbook.mapper.UserAddressMapper;
import com.example.addressbook.repository.UserAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserAddressService.
 */
@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @Mock
    private UserAddressRepository repository;

    @Mock
    private UserAddressMapper mapper;

    @InjectMocks
    private UserAddressService service;

    private UserAddress testEntity;
    private UserAddressDTO testDTO;

    @BeforeEach
    void setUp() {
        testEntity = UserAddress.builder()
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
    @DisplayName("Create Address Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create address successfully")
        void shouldCreateAddressSuccessfully() {
            when(mapper.toEntity(any(UserAddressDTO.class))).thenReturn(testEntity);
            when(repository.save(any(UserAddress.class))).thenReturn(testEntity);
            when(mapper.toDTO(any(UserAddress.class))).thenReturn(testDTO);

            UserAddressDTO result = service.create(testDTO);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("John Doe");
            verify(repository, times(1)).save(any(UserAddress.class));
        }
    }

    @Nested
    @DisplayName("Get Address Tests")
    class GetTests {

        @Test
        @DisplayName("Should get address by ID successfully")
        void shouldGetAddressByIdSuccessfully() {
            when(repository.findById(1L)).thenReturn(Optional.of(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            UserAddressDTO result = service.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should throw exception when address not found")
        void shouldThrowExceptionWhenNotFound() {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.getById(999L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("999");
        }

        @Test
        @DisplayName("Should get all addresses")
        void shouldGetAllAddresses() {
            UserAddress anotherEntity = UserAddress.builder()
                    .id(2L)
                    .name("Jane Doe")
                    .build();
            UserAddressDTO anotherDTO = UserAddressDTO.builder()
                    .id(2L)
                    .name("Jane Doe")
                    .build();

            when(repository.findAll()).thenReturn(Arrays.asList(testEntity, anotherEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);
            when(mapper.toDTO(anotherEntity)).thenReturn(anotherDTO);

            List<UserAddressDTO> result = service.getAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("John Doe");
            assertThat(result.get(1).getName()).isEqualTo("Jane Doe");
        }
    }

    @Nested
    @DisplayName("Update Address Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update address successfully")
        void shouldUpdateAddressSuccessfully() {
            UserAddressDTO updateDTO = UserAddressDTO.builder()
                    .name("John Updated")
                    .email("updated@example.com")
                    .build();

            when(repository.findById(1L)).thenReturn(Optional.of(testEntity));
            when(repository.save(any(UserAddress.class))).thenReturn(testEntity);
            when(mapper.toDTO(any(UserAddress.class))).thenReturn(updateDTO);

            UserAddressDTO result = service.update(1L, updateDTO);

            assertThat(result).isNotNull();
            verify(mapper, times(1)).updateEntity(updateDTO, testEntity);
            verify(repository, times(1)).save(testEntity);
        }

        @Test
        @DisplayName("Should throw exception when updating non-existent address")
        void shouldThrowExceptionWhenUpdatingNonExistent() {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.update(999L, testDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Delete Address Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete address successfully")
        void shouldDeleteAddressSuccessfully() {
            when(repository.existsById(1L)).thenReturn(true);
            doNothing().when(repository).deleteById(1L);

            service.delete(1L);

            verify(repository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existent address")
        void shouldThrowExceptionWhenDeletingNonExistent() {
            when(repository.existsById(999L)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(999L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Search Address Tests")
    class SearchTests {

        @Test
        @DisplayName("Should search addresses by keyword")
        void shouldSearchAddressesByKeyword() {
            when(repository.searchByKeyword("John")).thenReturn(Arrays.asList(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            List<UserAddressDTO> result = service.search("John");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should return all addresses when keyword is empty")
        void shouldReturnAllWhenKeywordEmpty() {
            when(repository.findAll()).thenReturn(Arrays.asList(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            List<UserAddressDTO> result = service.search("");

            assertThat(result).hasSize(1);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should return all addresses when keyword is null")
        void shouldReturnAllWhenKeywordNull() {
            when(repository.findAll()).thenReturn(Arrays.asList(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            List<UserAddressDTO> result = service.search(null);

            assertThat(result).hasSize(1);
            verify(repository, times(1)).findAll();
        }

        @Test
        @DisplayName("Should find addresses by name")
        void shouldFindAddressesByName() {
            when(repository.findByNameContainingIgnoreCase("John")).thenReturn(Arrays.asList(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            List<UserAddressDTO> result = service.findByName("John");

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should find addresses by city")
        void shouldFindAddressesByCity() {
            when(repository.findByCityIgnoreCase("New York")).thenReturn(Arrays.asList(testEntity));
            when(mapper.toDTO(testEntity)).thenReturn(testDTO);

            List<UserAddressDTO> result = service.findByCity("New York");

            assertThat(result).hasSize(1);
        }
    }
}
