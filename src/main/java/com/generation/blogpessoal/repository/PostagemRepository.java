package com.generation.blogpessoal.repository;

import java.util.List;  // ← NOVO import (necessário para usar List)

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;  // ← NOVO import

import com.generation.blogpessoal.model.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{
    
    // Este método já existia? Se sim, mantenha. Se não, pode adicionar.
    // public List <Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
    
    // NOVO MÉTODO - Adicione APENAS esta linha abaixo
    public List<Postagem> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);

}