package com.example.addressbook.controller;

import com.example.addressbook.dto.UserAddressDTO;
import com.example.addressbook.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user addresses.
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "User Address", description = "User Address Directory API")
public class UserAddressController {

    private final UserAddressService service;

    /**
     * Creates a new user address.
     */
    @PostMapping
    @Operation(summary = "Create a new address", description = "Creates a new user address entry")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<UserAddressDTO> create(@Valid @RequestBody UserAddressDTO dto) {
        UserAddressDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Gets an address by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get address by ID", description = "Retrieves a user address by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<UserAddressDTO> getById(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * Gets all addresses.
     */
    @GetMapping
    @Operation(summary = "Get all addresses", description = "Retrieves all user addresses")
    @ApiResponse(responseCode = "200", description = "List of addresses")
    public ResponseEntity<List<UserAddressDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Updates an existing address.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update address", description = "Updates an existing user address")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<UserAddressDTO> update(
            @Parameter(description = "Address ID") @PathVariable Long id,
            @Valid @RequestBody UserAddressDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Deletes an address.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete address", description = "Deletes a user address")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Address ID") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Searches addresses by keyword.
     */
    @GetMapping("/search")
    @Operation(summary = "Search addresses", description = "Searches addresses by keyword across all fields")
    @ApiResponse(responseCode = "200", description = "Search results")
    public ResponseEntity<List<UserAddressDTO>> search(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String q) {
        return ResponseEntity.ok(service.search(q));
    }

    /**
     * Searches addresses by name.
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search by name", description = "Searches addresses by name")
    @ApiResponse(responseCode = "200", description = "Search results")
    public ResponseEntity<List<UserAddressDTO>> searchByName(
            @Parameter(description = "Name to search") @RequestParam String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    /**
     * Searches addresses by city.
     */
    @GetMapping("/search/city")
    @Operation(summary = "Search by city", description = "Searches addresses by city")
    @ApiResponse(responseCode = "200", description = "Search results")
    public ResponseEntity<List<UserAddressDTO>> searchByCity(
            @Parameter(description = "City to search") @RequestParam String city) {
        return ResponseEntity.ok(service.findByCity(city));
    }
}
