package com.example.ecommerce_spring_react.service;

import com.example.ecommerce_spring_react.model.Usuario;
import com.example.ecommerce_spring_react.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    // Post de Usuario
    public Usuario cadastrarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new Exception("Email já cadastrado");
        }
        // criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // garante que o papel esteja no formato correto
        if (usuario.getPapel() == null || usuario.getPapel().isEmpty()){
            usuario.setPapel("ROLE_USER");//padrão
        } else if (!usuario.getPapel().startsWith("ROLE_")) {
            usuario.setPapel("ROLE_" + usuario.getPapel().toUpperCase());
        }

        return usuarioRepository.save(usuario);
    }

    // buscar usuario por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // verificar senha
    public boolean verificarSenha(String senhaDigitada, String senhaCriptografada) {
        return passwordEncoder.matches(senhaDigitada, senhaCriptografada);
    }
}
