package marpetplace.api.service;

import marpetplace.api.domain.entity.Admin;
import marpetplace.api.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminAuthenticationServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminAuthenticationService adminAuthenticationService;

    private Admin admin;
    private String username;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        username = "admin";
        admin = new Admin();
        admin.setLogin(username);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(adminRepository.findByLogin(username)).thenReturn(admin);

        UserDetails result = adminAuthenticationService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(adminRepository, times(1)).findByLogin(username);
    }

    @Test
    void testLoadUserByUsernameThrowsUsernameNotFoundException() {
        when(adminRepository.findByLogin(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            adminAuthenticationService.loadUserByUsername(username);
        });

        verify(adminRepository, times(1)).findByLogin(username);
    }
}

