package com.example.ecommerce_spring_react.service;

import com.example.ecommerce_spring_react.model.Produto;
import com.example.ecommerce_spring_react.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id){
        return produtoRepository.findById(id);
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtt){
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(produtoAtt.getNome());
                    produto.setDescricao(produtoAtt.getDescricao());
                    produto.setPreco(produtoAtt.getPreco());
                    produto.setEstoque(produtoAtt.getEstoque());
                    produto.setImagemUrl(produtoAtt.getImagemUrl());
                    return produtoRepository.save(produto);
                })
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void deletar(Long id){
        produtoRepository.deleteById(id);
    }
}
