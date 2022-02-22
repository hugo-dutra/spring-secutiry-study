package com.hugodutra.study.controller;

import com.hugodutra.study.entities.Usuario;
import com.hugodutra.study.security.PasswordManagerService;
import com.hugodutra.study.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PasswordManagerService passwordManagerService;

    @GetMapping("/{login}")
    public ResponseEntity<Usuario> findByLogin(@PathVariable String login) {
        Optional<Usuario> usuario = usuarioService.findByLogin((login));
        return usuario.isPresent() ?
                ResponseEntity.ok(usuario.get()) :
                ResponseEntity.noContent().build();
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> usuarios = usuarioService.findAll();
        return usuarios.size() != 0 ?
                ResponseEntity.ok(usuarios) :
                ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordManagerService.getPasswordEncoder().encode(usuario.getPassword()));
        Usuario novoUsuario = usuarioService.create(usuario);
        return ResponseEntity.ok(novoUsuario);
    }

    @GetMapping("/validarSenha")
    public ResponseEntity<Boolean> validar(@RequestBody Usuario usuario) {
        Optional<Usuario> optUsuario = usuarioService.findByLogin(usuario.getLogin());
        if (!optUsuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        Boolean valid = passwordManagerService
                .getPasswordEncoder()
                .matches(usuario.getPassword(), optUsuario.get().getPassword());
        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(valid);
    }

}
