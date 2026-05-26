package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.ManyToOne; // Anotação para relacionamento Muitos-para-Um
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Evita loop infinito na serialização JSON

@Entity // Indica que esta classe é uma entidade JPA, será convertida em tabela no banco
@Table(name = "tb_postagens") // Define o nome da tabela no banco de dados
public class Postagem {	

    @Id // Define este campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento (gerado pelo banco)
    private Long id;

    @NotBlank(message = "O atributo título é Obrigatório!") // Não pode ser nulo, vazio ou só espaços
    @Size(min = 5, max = 100, message = "O atributo título deve conter no mínimo 05 e no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "O atributo texto é Obrigatório!")
    @Size(min = 10, max = 1000, message = "O atributo texto deve conter no mínimo 10 e no máximo 1000 caracteres")
    private String texto;

    @UpdateTimestamp // Atualiza automaticamente com a data/hora atual ao modificar o registro
    private LocalDateTime data;

    // RELACIONAMENTO COM A CLASSE TEMA
    // Muitas Postagens podem ter um único Tema (N:1)
    @ManyToOne
    @JsonIgnoreProperties("postagem") // Ignora o campo "postagem" da classe Tema ao serializar, evitando loop infinito
    private Tema tema; // Chave estrangeira: tema_id na tabela tb_postagens

    // GETTERS E SETTERS (métodos para acessar e modificar os atributos)
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
}