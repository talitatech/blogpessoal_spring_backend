// Pacote onde a classe Controller está localizada
package com.generation.blogpessoal.controller;

// Importações das classes necessárias
import java.util.List;                              // Para usar lista de objetos
import java.util.Optional;                          // Para evitar NullPointerException

import org.springframework.beans.factory.annotation.Autowired; // Para injeção de dependência
import org.springframework.http.HttpStatus;         // Para usar os status HTTP (200, 404, etc.)
import org.springframework.http.ResponseEntity;     // Para personalizar a resposta HTTP
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;  // Para mapear requisições DELETE
import org.springframework.web.bind.annotation.GetMapping;    // Para mapear requisições GET
import org.springframework.web.bind.annotation.PathVariable;  // Para capturar variáveis da URL
import org.springframework.web.bind.annotation.PostMapping;   // Para mapear requisições POST
import org.springframework.web.bind.annotation.PutMapping;    // Para mapear requisições PUT
import org.springframework.web.bind.annotation.RequestBody;   // Para capturar o corpo da requisição
import org.springframework.web.bind.annotation.RequestMapping; // Para definir a URL base
import org.springframework.web.bind.annotation.ResponseStatus; // Para definir status HTTP da resposta
import org.springframework.web.bind.annotation.RestController; // Para indicar que é um Controller REST
import org.springframework.web.server.ResponseStatusException; // Para lançar exceções com status HTTP

import com.generation.blogpessoal.model.Postagem;   // Importa a classe Postagem (Model)
import com.generation.blogpessoal.repository.PostagemRepository; // Importa o Repository
import com.generation.blogpessoal.repository.TemaRepository; // Importa o Repository do Tema (NOVO)
import jakarta.validation.Valid;                    // Para validar o objeto com @Valid

// @RestController indica que esta classe é um Controlador REST
// Ela vai receber requisições HTTP e devolver respostas (geralmente em JSON)
@RestController

// @RequestMapping define a URL BASE para TODOS os endpoints deste Controller
// Exemplo: localhost:8080/postagens
@RequestMapping("/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    // @Autowired é a Injeção de Dependência
    // O Spring cria automaticamente um objeto do PostagemRepository para a gente usar
    // Assim não precisamos dar "new" (instanciar manualmente)
    @Autowired
    private PostagemRepository postagemRepository;
    
    // NOVO: Injeção de dependência do TemaRepository
    // Necessário para validar se o Tema existe antes de salvar/atualizar uma Postagem
    @Autowired
    private TemaRepository temaRepository;

    // ================================================================
    // MÉTODO 1: getAll() - Listar TODAS as postagens
    // ================================================================
    
    // @GetMapping indica que este método responde a requisições HTTP do tipo GET
    // Sem caminho adicional, usa a URL base definida no @RequestMapping
    // URL: GET http://localhost:8080/postagens
    @GetMapping
    public ResponseEntity<List<Postagem>> getAll() {
        
        // findAll() é um método padrão do JpaRepository
        // Ele busca TODOS os registros da tabela tb_postagens
        // Retorna uma Lista de objetos Postagem
        
        // ResponseEntity.ok() monta uma resposta HTTP com:
        // - Status 200 OK (sucesso)
        // - Corpo da resposta = lista de postagens
        return ResponseEntity.ok(postagemRepository.findAll());
    }

    // ================================================================
    // MÉTODO 2: getById() - Buscar uma postagem ESPECÍFICA pelo ID
    // ================================================================
    
    // @GetMapping("/{id}") indica que este método responde a GET com um valor na URL
    // O {id} é uma variável que será capturada do caminho da URL
    // URL: GET http://localhost:8080/postagens/1  (busca a postagem com id = 1)
    @GetMapping("/{id}")
    public ResponseEntity<Postagem> getById(@PathVariable Long id) {
        
        // @PathVariable pega o valor que veio na URL (ex: 1) e coloca na variável 'id'
        
        // postagemRepository.findById(id) - Busca no banco uma postagem pelo ID
        // Retorna um Optional (pode conter a postagem ou estar vazio)
        // Optional evita o erro NullPointerException (objeto nulo)
        
        return postagemRepository.findById(id)
                // .map() - Se a postagem EXISTIR, executa o código dentro
                // 'resposta' é a postagem encontrada
                // ResponseEntity.ok(resposta) cria resposta com Status 200 OK + a postagem
                .map(resposta -> ResponseEntity.ok(resposta))
                
                // .orElse() - Se a postagem NÃO EXISTIR, executa o código dentro
                // ResponseEntity.status(HttpStatus.NOT_FOUND).build() cria resposta com:
                // - Status 404 NOT FOUND (não encontrado)
                // - Corpo vazio
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ================================================================
    // MÉTODO 3: getByTitulo() - Buscar postagens pelo TÍTULO
    // ================================================================
    
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
        return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
    }

    // ================================================================
    // MÉTODO 4: post() - Criar UMA NOVA postagem (INSERT no banco)
    // ================================================================
    
    // @PostMapping indica que este método responde a requisições HTTP do tipo POST
    // URL: POST http://localhost:8080/postagens
    @PostMapping
    public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem) {
        
        // @Valid - Valida o objeto conforme as regras da Classe Model (Postagem)
        // @RequestBody - Converte o JSON enviado no corpo da requisição para um objeto Postagem
        
        // NOVO: Verifica se o Tema existe antes de salvar a Postagem
        // postagem.getTema().getId() - pega o id do tema enviado no JSON
        // temaRepository.existsById() - verifica se esse id existe no banco
        if (temaRepository.existsById(postagem.getTema().getId())) {
            
            // Define o id da postagem como null para evitar enviar valor padrão 0
            // Isso garante que será feita uma INSERÇÃO e não uma ATUALIZAÇÃO
            postagem.setId(null);
            
            // ResponseEntity.status(HttpStatus.CREATED) - Retorna status 201 (CREATED)
            // .body(postagemRepository.save(postagem)) - Salva no banco e retorna o objeto salvo no corpo da resposta
            return ResponseEntity.status(HttpStatus.CREATED).body(postagemRepository.save(postagem));
        }
        
        // Se o Tema NÃO existir, lança uma exceção com status 400 BAD REQUEST
        // A mensagem "Tema não existe!" ajuda a identificar o erro
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
    }

    // ================================================================
    // MÉTODO 5: put() - ATUALIZAR uma postagem existente (UPDATE no banco)
    // ================================================================
    
    // @PutMapping indica que este método responde a requisições HTTP do tipo PUT
    // URL: PUT http://localhost:8080/postagens
    @PutMapping
    public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem) {
        
        // @Valid - Valida o objeto conforme as regras da Classe Model (Postagem)
        // @RequestBody - Converte o JSON enviado no corpo da requisição para um objeto Postagem
        
        // NOVO: Verifica se a Postagem existe antes de tentar atualizar
        if (postagemRepository.existsById(postagem.getId())) {
            
            // NOVO: Verifica se o Tema existe antes de atualizar a Postagem
            if (temaRepository.existsById(postagem.getTema().getId())) {
                
                // Se a Postagem existe E o Tema existe, atualiza e retorna status 200 OK
                return ResponseEntity.status(HttpStatus.OK).body(postagemRepository.save(postagem));
            }
            
            // Se o Tema NÃO existir, lança exceção com status 400 BAD REQUEST
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe!", null);
        }
        
        // Se a Postagem NÃO existir, retorna status 404 NOT FOUND
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // ================================================================
    // MÉTODO 6: delete() - DELETAR uma postagem pelo ID
    // ================================================================
    
    // @ResponseStatus(HttpStatus.NO_CONTENT) - Define que a resposta terá status 204 (NO CONTENT)
    // Isso significa: "Deu certo, mas não tenho conteúdo para retornar"
    @ResponseStatus(HttpStatus.NO_CONTENT)
    
    // @DeleteMapping("/{id}") - Mapeia requisições DELETE com um ID na URL
    // URL: DELETE http://localhost:8080/postagens/1  (deleta a postagem com id = 1)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        
        // Optional<Postagem> postagem - Pode conter a postagem ou estar vazio
        Optional<Postagem> postagem = postagemRepository.findById(id);

        // Se a postagem NÃO EXISTIR (isEmpty = verdadeiro)
        // Lança uma exceção com status 404 NOT FOUND
        if(postagem.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // Se a postagem EXISTIR, deleta pelo ID
        postagemRepository.deleteById(id);
    }
}