package com.ej.authentication.jwt;

import com.ej.authentication.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    private Usuario usuario;

    public JwtUserDetails(Usuario usuario) {
        super(usuario.getUsername(), usuario.getPassword(), AuthorityUtils.createAuthorityList(usuario.getRole().name()));
        this.usuario = usuario;
    }

    public String getUsername(){
        return this.usuario.getUsername();
    }

    public Long getId(){
        return this.usuario.getId();
    }

    public String getRole(){
        return this.usuario.getRole().name();
    }

}
