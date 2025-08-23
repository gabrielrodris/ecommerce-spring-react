package com.example.ecommerce_spring_react.repository;

import com.example.ecommerce_spring_react.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuário pelo email (necessário para login)
        Optional<Usuario> findByEmail(String email);
    // Verifica se email já existe
        boolean existsByEmail(String email);
}
