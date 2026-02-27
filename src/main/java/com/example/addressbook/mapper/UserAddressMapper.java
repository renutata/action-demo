package com.example.addressbook.mapper;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.entity.UserAddress;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between UserAddress entity and UserAddressDTO.
 */
@Component
public class UserAddressMapper {

    /**
     * Converts a UserAddress entity to a UserAddressDTO.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    public UserAddressDTO toDTO(UserAddress entity) {
        if (entity == null) {
            return null;
        }

        return UserAddressDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .street(entity.getStreet())
                .city(entity.getCity())
                .state(entity.getState())
                .zipCode(entity.getZipCode())
                .country(entity.getCountry())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Converts a UserAddressDTO to a UserAddress entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    public UserAddress toEntity(UserAddressDTO dto) {
        if (dto == null) {
            return null;
        }

        return UserAddress.builder()
                .id(dto.getId())
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .street(dto.getStreet())
                .city(dto.getCity())
                .state(dto.getState())
                .zipCode(dto.getZipCode())
                .country(dto.getCountry())
                .build();
    }

    /**
     * Updates an existing entity with values from a DTO.
     *
     * @param dto    the DTO with new values
     * @param entity the entity to update
     */
    public void updateEntity(UserAddressDTO dto, UserAddress entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setStreet(dto.getStreet());
        entity.setCity(dto.getCity());
        entity.setState(dto.getState());
        entity.setZipCode(dto.getZipCode());
        entity.setCountry(dto.getCountry());
    }
}
