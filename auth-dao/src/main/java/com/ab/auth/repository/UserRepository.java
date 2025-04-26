package com.ab.auth.repository;

import com.ab.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM auth_service_user_ms_tbl WHERE email = ?1 and is_deleted=false", nativeQuery = true)
    User findActiveUserByEmail(@Param("email") String email);

    @Query(value = "SELECT email FROM auth_service_user_ms_tbl WHERE is_deleted = false;", nativeQuery = true)
    String[] findAllUsersEmails();

}
