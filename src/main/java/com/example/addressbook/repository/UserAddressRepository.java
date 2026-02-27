package com.example.addressbook.repository;

import com.example.addressbook.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for UserAddress entity.
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    /**
     * Search for addresses by keyword across multiple fields.
     *
     * @param keyword the search keyword
     * @return list of matching addresses
     */
    @Query("SELECT u FROM UserAddress u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.street) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.state) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.country) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<UserAddress> searchByKeyword(@Param("keyword") String keyword);

    /**
     * Find addresses by name containing the given string (case-insensitive).
     */
    List<UserAddress> findByNameContainingIgnoreCase(String name);

    /**
     * Find addresses by city (case-insensitive).
     */
    List<UserAddress> findByCityIgnoreCase(String city);

    /**
     * Find addresses by email (case-insensitive).
     */
    List<UserAddress> findByEmailIgnoreCase(String email);
}
