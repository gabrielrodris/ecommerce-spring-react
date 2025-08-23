package com.example.ecommerce_spring_react.controller;

import com.example.ecommerce_spring_react.dto.LoginRequest;
import com.example.ecommerce_spring_react.dto.LoginResponse;
import com.example.ecommerce_spring_react.model.Usuario;
import com.example.ecommerce_spring_react.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final String jwtSecret = "U0VDUkVUX1NFTkNPRElHRV9FWEFNUExF"; // Base64
    private final long jwtExpirationMs = 86400000; // 24h

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.ok(novoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.buscarPorEmail(loginRequest.getEmail())
                .map(usuario -> {
                    if (usuarioService.verificarSenha(loginRequest.getSenha(), usuario.getSenha())) {

                        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);//gera chave forte automaticamente

                        String token = Jwts.builder()
                                .setSubject(usuario.getEmail())
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                                .signWith(key)
                                .compact();

                        return ResponseEntity.ok(new LoginResponse(token, usuario.getEmail(),
                                usuario.getNome(), usuario.getPapel()));
                    } else {
                        return ResponseEntity.status(401).body("Senha inválida");
                    }
                })
                .orElse(ResponseEntity.status(404).body("Usuário não encontrado"));
    }
}
