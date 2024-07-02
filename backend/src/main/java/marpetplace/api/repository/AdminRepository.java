package marpetplace.api.repository;

import marpetplace.api.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    //Authentication
    Admin findByLogin(String login);
}
