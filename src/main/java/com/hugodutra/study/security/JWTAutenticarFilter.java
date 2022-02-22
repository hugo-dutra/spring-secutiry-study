package com.hugodutra.study.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugodutra.study.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RequiredArgsConstructor
public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    public static final int TOKEN_EXPIRACAO = 600000;
    public static final String TOKEN_SENHA = "50508591-112d-4478-a660-d06ee2b52db5";
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Usuario usuario = new ObjectMapper()
                    .readValue(request.getInputStream(),Usuario.class);
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword(),new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuário",e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        DetalheUsuarioData detalheUsuarioData = (DetalheUsuarioData) authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(detalheUsuarioData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+TOKEN_EXPIRACAO))
                .sign(Algorithm.HMAC512(TOKEN_SENHA));
        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
