package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	@DisplayName("Cadastrar um usuário")
	void deveCadastrarUsuario() {

		Usuario usuario = new Usuario();

		usuario.setNome("Maria da Silva");
		usuario.setUsuario("maria@email.com");
		usuario.setSenha("12345678");
		usuario.setFoto("https://i.imgur.com/teste.png");

		ResponseEntity<Usuario> resposta = testRestTemplate.postForEntity("/usuarios/cadastrar", usuario, Usuario.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}

	@Test
	@DisplayName("Não deve cadastrar usuário duplicado")
	void naoDeveCadastrarUsuarioDuplicado() {

		Usuario usuario = new Usuario();

		usuario.setNome("Joana Silva");
		usuario.setUsuario("joana@email.com");
		usuario.setSenha("12345678");
		usuario.setFoto("https://i.imgur.com/teste.png");

		testRestTemplate.postForEntity("/usuarios/cadastrar", usuario, Usuario.class);

		ResponseEntity<Usuario> resposta = testRestTemplate.postForEntity("/usuarios/cadastrar", usuario, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
}