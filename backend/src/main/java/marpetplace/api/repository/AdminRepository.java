package marpetplace.api.repository;

import marpetplace.api.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Admin getByLogin(String login);

    //Authentication
    Admin findByLogin(String login);
}
