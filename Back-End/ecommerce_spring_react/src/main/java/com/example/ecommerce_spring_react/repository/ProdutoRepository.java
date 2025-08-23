package com.example.ecommerce_spring_react.repository;

import com.example.ecommerce_spring_react.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
