package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp; // Atualiza automaticamente a data/hora ao modificar o registro

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Evita loop infinito na serialização JSON (evita erro de recursão)

import jakarta.persistence.Column; // Define características da coluna no banco de dados
import jakarta.persistence.Entity; // Indica que a classe é uma entidade JPA (será uma tabela no banco)
import jakarta.persistence.GeneratedValue; // Define que o valor é gerado automaticamente
import jakarta.persistence.GenerationType; // Define a estratégia de geração do valor (IDENTITY = auto-incremento)
import jakarta.persistence.Id; // Define a chave primária da tabela
import jakarta.persistence.ManyToOne; // Define relacionamento muitos-para-um (N:1)
import jakarta.persistence.Table; // Define o nome da tabela no banco de dados
import jakarta.validation.constraints.NotBlank; // Valida que o campo não pode ser nulo, vazio ou conter apenas espaços
import jakarta.validation.constraints.Pattern; // Valida se o valor corresponde a uma expressão regular
import jakarta.validation.constraints.Size; // Valida o tamanho mínimo e máximo do campo

@Entity // Esta classe será convertida em tabela no banco de dados
@Table(name = "tb_postagens") // Nome da tabela no banco de dados
public class Postagem {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Valor gerado automaticamente pelo banco (auto-incremento)
    private Long id;
    
    @Column(length = 100) // Define que a coluna terá tamanho máximo de 100 caracteres no banco
    @NotBlank(message = "O atributo título é obrigatório!") // Campo obrigatório
    @Size(min = 5, max = 100, message = "O atributo título deve ter no minimo 5 e no máximo 100 caracteres.") // Valida tamanho mínimo e máximo
    @Pattern(regexp = "^[^0-9].*", message = "O título não pode ser apenas numérico") // Impede que o título seja apenas números
    private String titulo;
    
    @Column(length = 1000) // Define que a coluna terá tamanho máximo de 1000 caracteres no banco
    @NotBlank(message = "O atributo texto é obrigatório!") // Campo obrigatório
    @Size(min = 10, max = 1000, message = "O atributo texto deve ter no minimo 10 e no máximo 1000 caracteres.") // Valida tamanho mínimo e máximo
    @Pattern(regexp = "^[^0-9].*", message = "O texto não pode ser apenas numérico") // Impede que o texto seja apenas números
    private String texto;
    
    @UpdateTimestamp // Preenche automaticamente com a data/hora atual ao criar ou atualizar o registro
    private LocalDateTime data;

    @ManyToOne // Relacionamento Muitos-para-Um: muitas postagens podem ter o mesmo tema
    @JsonIgnoreProperties("postagem") // Durante a serialização, ignora o campo "postagem" da classe Tema para evitar loop infinito
    private Tema tema; // Chave estrangeira: tema_id na tabela tb_postagens
    
    @ManyToOne // Relacionamento Muitos-para-Um: muitas postagens podem ter o mesmo usuário
    @JsonIgnoreProperties("postagem") // Durante a serialização, ignora o campo "postagem" da classe Usuario para evitar loop infinito
    private Usuario usuario; // Chave estrangeira: usuario_id na tabela tb_postagens
    
    // GETTERS E SETTERS: métodos para acessar (get) e modificar (set) os atributos privados
    
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}