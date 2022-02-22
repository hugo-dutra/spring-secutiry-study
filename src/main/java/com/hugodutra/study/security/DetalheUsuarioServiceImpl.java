package com.hugodutra.study.security;

import com.hugodutra.study.entities.Usuario;
import com.hugodutra.study.repositories.UsuarioRepository;
import com.hugodutra.study.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetalheUsuarioServiceImpl implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioService.findByLogin(username);
        if(!usuario.isPresent()){
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new DetalheUsuarioData(usuario);
    }
}
