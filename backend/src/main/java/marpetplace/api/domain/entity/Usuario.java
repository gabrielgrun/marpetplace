package marpetplace.api.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import marpetplace.api.domain.UsuarioStatus;
import marpetplace.api.dto.request.UsuarioRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 255)
    private String nome;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "senha", length = 100)
    private String senha;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UsuarioStatus status;

    public Usuario(UsuarioRequest usuarioRequest){
        this.nome = usuarioRequest.nome();
        this.email = usuarioRequest.email();
        this.senha = usuarioRequest.senha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UsuarioStatus.ATIVO;
    }
}

