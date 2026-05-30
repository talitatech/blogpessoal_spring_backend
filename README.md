# Blog Pessoal - Spring Backend

API REST para plataforma de blog com autenticação JWT, desenvolvida em Java com Spring Boot.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Branches e Etapas](#branches-e-etapas)
- [Endpoints da API](#endpoints-da-api)
- [Configuração e Execução](#configuracao-e-execucao)
- [Autenticação](#autenticacao)
- [Banco de Dados](#banco-de-dados)

## 🎯 Sobre o Projeto

O **Blog Pessoal** é uma API REST desenvolvida como projeto acadêmico para simular o backend de uma plataforma de blog, permitindo o gerenciamento de:

- **Usuários** - cadastro, autenticação e perfil
- **Postagens** - criação, edição, listagem e remoção
- **Temas** - categorização das postagens

O projeto evoluiu em etapas incrementais, desde o CRUD básico até a implementação completa de segurança com Spring Security e JWT.

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|------------|--------|-------------|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.5.14 | Framework principal |
| Spring Web | - | Endpoints REST |
| Spring Data JPA | - | Persistência de dados |
| Spring Security | - | Autenticação e autorização |
| JWT (JJWT) | 0.12.6 | Token de autenticação |
| Hibernate | - | ORM |
| MySQL | - | Banco de dados relacional |
| Maven | - | Gerenciador de dependências |
| Jakarta Validation | - | Validação de dados |

## 📁 Estrutura do Projeto

```
src/main/java/com/generation/blogpessoal/
├── BlogpessoalApplication.java          # Classe principal
├── controller/
│   ├── PostagemController.java          # Endpoints de postagens
│   ├── TemaController.java              # Endpoints de temas
│   └── UsuarioController.java           # Endpoints de usuários e login
├── model/
│   ├── Postagem.java                    # Entidade Postagem
│   ├── Tema.java                        # Entidade Tema
│   ├── Usuario.java                     # Entidade Usuario
│   └── UsuarioLogin.java                # DTO para login
├── repository/
│   ├── PostagemRepository.java          # CRUD postagens
│   ├── TemaRepository.java              # CRUD temas
│   └── UsuarioRepository.java           # CRUD usuários + findByUsuario
├── security/
│   ├── BasicSecurityConfig.java         # Configuração do Spring Security
│   ├── JwtAuthFilter.java               # Filtro para validação do token
│   ├── JwtService.java                  # Geração e validação do JWT
│   ├── UserDetailsImpl.java             # Implementação do UserDetails
│   └── UserDetailsServiceImpl.java      # Busca usuário no banco
└── resources/
    └── application.properties           # Configurações do banco (ignorado no Git)
```

## 🌿 Branches e Etapas

O repositório foi organizado em branches para cada fase de aprendizado:

### `parte-2-crud`
Implementação das operações básicas do sistema:
- CRUD completo para Usuario, Postagem e Tema
- Mapeamento JPA básico
- Endpoints GET, POST, PUT e DELETE

### `parte-3-relacionamento`
Adição de relacionamentos entre entidades:
- `@OneToMany` e `@ManyToOne` entre Usuario ↔ Postagem
- `@OneToMany` e `@ManyToOne` entre Tema ↔ Postagem
- `@JsonIgnoreProperties` para evitar loops na serialização

### `parte-4-seguranca-jwt` ⭐ (branch atual)
Implementação completa de segurança:
- Spring Security com autenticação JWT
- Criptografia de senhas com BCrypt
- Endpoint de login (`/usuarios/logar`)
- Geração e validação de tokens JWT
- Proteção de endpoints com token Bearer
- Filtro personalizado `JwtAuthFilter`

## 🔗 Endpoints da API

### Endpoints Públicos (não exigem token)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/usuarios/cadastrar` | Cadastra um novo usuário |
| POST | `/usuarios/logar` | Realiza login e retorna token JWT |

### Endpoints Protegidos (exigem token)

#### Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/usuarios` | Lista todos os usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| PUT | `/usuarios` | Atualiza usuário |
| DELETE | `/usuarios/{id}` | Remove usuário |

#### Postagens
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/postagens` | Lista todas as postagens |
| GET | `/postagens/{id}` | Busca postagem por ID |
| GET | `/postagens/titulo/{titulo}` | Busca por título |
| POST | `/postagens` | Cria nova postagem |
| PUT | `/postagens` | Atualiza postagem |
| DELETE | `/postagens/{id}` | Remove postagem |

#### Temas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/temas` | Lista todos os temas |
| GET | `/temas/{id}` | Busca tema por ID |
| GET | `/temas/nome/{descricao}` | Busca por descrição |
| POST | `/temas` | Cria novo tema |
| PUT | `/temas` | Atualiza tema |
| DELETE | `/temas/{id}` | Remove tema |

## 🔐 Autenticação

### Login

**Requisição:**
```http
POST /usuarios/logar
Content-Type: application/json

{
  "usuario": "email@exemplo.com",
  "senha": "12345678"
}
```

**Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "usuario": "email@exemplo.com",
  "senha": "",
  "foto": null,
  "token": "Bearer eyJhbGciOiJIUzI1NiIs..."
}
```

### Acessando Endpoints Protegidos

Incluir o token no cabeçalho `Authorization`:

```http
GET /postagens
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

## 🗄️ Banco de Dados

### Modelo Relacional

```
tb_usuarios (id, nome, usuario, senha, foto)
       │
       │ (1:N)
       ↓
tb_postagens (id, titulo, texto, data, usuario_id, tema_id)
       ↑
       │ (N:1)
       │
tb_temas (id, descricao)
```

### Diagrama de Entidades

| Entidade | Tabela | PK | FKs |
|----------|--------|----|-----|
| Usuario | `tb_usuarios` | `id` | - |
| Postagem | `tb_postagens` | `id` | `usuario_id`, `tema_id` |
| Tema | `tb_temas` | `id` | - |

## ⚙️ Configuração e Execução

### Pré-requisitos

- Java 17+
- MySQL
- Maven
- Git

### Passo a passo

1. **Clonar o repositório**
```bash
git clone https://github.com/talitatech/blogpessoal_spring_backend.git
cd blogpessoal_spring_backend
```

2. **Trocar para a branch da segurança**
```bash
git checkout parte-4-seguranca-jwt
```

3. **Criar o arquivo `application.properties`** em `src/main/resources/`:
```properties
# Configuração do Banco de Dados
spring.datasource.url=jdbc:mysql://localhost:3306/db_blogpessoal
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

4. **Criar o banco de dados** no MySQL:
```sql
CREATE DATABASE db_blogpessoal;
```

5. **Executar o projeto**
```bash
mvn spring-boot:run
```

6. **Acessar** - O servidor estará rodando em `http://localhost:8080`

### Testando com Insomnia/Postman

1. **Cadastrar usuário:** `POST /usuarios/cadastrar`
2. **Fazer login:** `POST /usuarios/logar` (copiar o token da resposta)
3. **Acessar endpoints:** Adicionar cabeçalho `Authorization: Bearer <token>`

## 📊 Evolução do Projeto

```
parte-2-crud (CRUD básico)
      ↓
parte-3-relacionamento (CRUD + relacionamentos)
      ↓
parte-4-seguranca-jwt (CRUD + relacionamentos + autenticação JWT)
```

## 👩‍💻 Autora

**Talita** - [GitHub](https://github.com/talitatech)

---

Projeto desenvolvido durante o curso da **Generation Brasil** - Turma 82
```
