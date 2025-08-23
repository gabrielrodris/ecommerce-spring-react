package com.example.ecommerce_spring_react.controller;

import com.example.ecommerce_spring_react.dto.LoginRequest;
import com.example.ecommerce_spring_react.dto.LoginResponse;
import com.example.ecommerce_spring_react.model.Usuario;
import com.example.ecommerce_spring_react.service.UsuarioService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final String jwtSecret = "segredo_super_secreto";
    private final long jwtExpirationMs = 86400000; // 24h

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try{
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
                        String token = Jwts.builder()
                                .setSubject(usuario.getEmail())
                                .setIssuedAt(new Date())
                                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                                .compact();

                        return ResponseEntity.ok(new LoginResponse(token, usuario.getEmail(), usuario.getNome(), usuario.getPapel()));
                    } else {
                        return ResponseEntity.status(401).body("Senha inválida");
                    }

                })
                .orElse(ResponseEntity.status(404).body("Usuário não encontrado"));
    }
}
