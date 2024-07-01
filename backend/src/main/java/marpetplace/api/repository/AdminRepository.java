package marpetplace.api.repository;

import marpetplace.api.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    //Authentication
    UserDetails findByLogin(String login);
}
