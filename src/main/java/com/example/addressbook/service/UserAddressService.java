package com.example.addressbook.service;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.entity.UserAddress;
import com.example.addressbook.exception.ResourceNotFoundException;
import com.example.addressbook.mapper.UserAddressMapper;
import com.example.addressbook.repository.UserAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing user addresses.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserAddressService {

    private final UserAddressRepository repository;
    private final UserAddressMapper mapper;

    /**
     * Creates a new user address.
     *
     * @param dto the address data
     * @return the created address
     */
    public UserAddressDTO create(UserAddressDTO dto) {
        UserAddress entity = mapper.toEntity(dto);
        UserAddress saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Gets a user address by ID.
     *
     * @param id the address ID
     * @return the address
     * @throws ResourceNotFoundException if not found
     */
    @Transactional(readOnly = true)
    public UserAddressDTO getById(Long id) {
        UserAddress entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAddress", id));
        return mapper.toDTO(entity);
    }

    /**
     * Gets all user addresses.
     *
     * @return list of all addresses
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing user address.
     *
     * @param id  the address ID
     * @param dto the updated data
     * @return the updated address
     * @throws ResourceNotFoundException if not found
     */
    public UserAddressDTO update(Long id, UserAddressDTO dto) {
        UserAddress existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAddress", id));

        mapper.updateEntity(dto, existing);
        UserAddress updated = repository.save(existing);
        return mapper.toDTO(updated);
    }

    /**
     * Deletes a user address.
     *
     * @param id the address ID
     * @throws ResourceNotFoundException if not found
     */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("UserAddress", id);
        }
        repository.deleteById(id);
    }

    /**
     * Searches for user addresses by keyword.
     *
     * @param keyword the search keyword
     * @return list of matching addresses
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }

        return repository.searchByKeyword(keyword.trim()).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds addresses by name.
     *
     * @param name the name to search for
     * @return list of matching addresses
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds addresses by city.
     *
     * @param city the city to search for
     * @return list of matching addresses
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> findByCity(String city) {
        return repository.findByCityIgnoreCase(city).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
