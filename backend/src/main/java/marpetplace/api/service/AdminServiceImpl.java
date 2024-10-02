package marpetplace.api.service;

import jakarta.transaction.Transactional;
import marpetplace.api.domain.entity.Admin;
import marpetplace.api.exception.EmailAlreadyRegisteredException;
import marpetplace.api.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    @Transactional
    public Admin register(Admin admin) {
        Admin adminFromDb = adminRepository.getByLogin(admin.getLogin());
        if (adminFromDb != null) {
            throw new EmailAlreadyRegisteredException();
        }

        String encryptedPassword = getEncryptedPassword(admin.getSenha());
        admin.setSenha(encryptedPassword);

        return adminRepository.save(admin);
    }

    private String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
