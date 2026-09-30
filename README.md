# API REST de Delivery de Comida Saudável

Back-end do recurso **Prato** (NutriExpress), em arquitetura de camadas:
`Controller → Service → Repository (Spring Data JPA) → PostgreSQL`.

## Tecnologias
- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- PostgreSQL 18
- Maven (wrapper incluso)

## Como rodar
1. Tenha um PostgreSQL rodando em `localhost:5432` com usuário `postgres` e senha `postgres`
   (o projeto usa o banco padrão `postgres`). Com Docker:
```
   docker run -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
```
2. Na raiz do projeto, execute `./mvnw spring-boot:run` (no Windows: `mvnw.cmd spring-boot:run`),
   ou rode a classe `DeliveryApplication` pela IDE.
3. A API sobe em `http://localhost:8080`. A tabela `pratos` é criada automaticamente na primeira execução.

A configuração do banco está em `src/main/resources/application.properties`.

## Endpoints

| Verbo | Rota | Descrição | Status |
|---|---|---|---|
| GET | `/pratos` | Lista todos os pratos | 200 |
| GET | `/pratos/{id}` | Busca um prato pelo id | 200 / 404 |
| GET | `/pratos?categoria=vegano` | Filtra pratos por categoria | 200 |
| POST | `/pratos` | Cria um prato | 201 / 400 / 409 |
| PUT | `/pratos/{id}` | Atualiza um prato | 200 / 400 / 404 / 409 |
| DELETE | `/pratos/{id}` | Remove um prato | 204 / 404 |

Exemplo de corpo (POST e PUT):

```json
{
  "nome": "Bowl Vegano de Grão-de-Bico",
  "descricao": "Grão-de-bico, quinoa, legumes assados e molho de tahine",
  "valor": 32.90,
  "categoria": "vegano",
  "calorias": 420,
  "quantidade": 350.0,
  "unidadeMedida": "g"
}
```

Categorias aceitas: `vegano`, `low carb`, `fitness`, `sobremesa saudável`. Unidades aceitas: `g` e `ml`.

## Regras e decisões
- **Regra de negócio própria** (`PratoService.validarNomeUnico`): não podem existir dois pratos com o mesmo nome
  (ignorando maiúsculas/minúsculas). A violação retorna `409 Conflict`.
- **Validações** no `PratoRequestDTO` (Bean Validation, ativadas com `@Valid`): campos obrigatórios,
  valor e quantidade positivos, categoria e unidade dentro dos valores permitidos. Dados inválidos retornam
  `400 Bad Request` com um mapa campo → mensagem.
- **PratoResponseDTO** expõe todos os campos do prato: todos são relevantes para o cliente de um app de delivery
  (preço, informações nutricionais e porção) e a entidade não possui campos internos a esconder. O DTO continua
  separado da entidade para não acoplar o contrato da API à estrutura da tabela.
- **Tratamento global de erros** (`GlobalExceptionHandler`, `@ControllerAdvice`): 400 (validação),
  404 (`PratoNaoEncontradoException`) e 409 (`PratoDuplicadoException`).

## Estrutura
- `controller/PratoController`
- `dto/PratoRequestDTO`, `dto/PratoResponseDTO` (Java Records)
- `exception/GlobalExceptionHandler`, `PratoNaoEncontradoException`, `PratoDuplicadoException`
- `model/Prato` (entidade JPA, tabela `pratos`)
- `repository/PratoRepository` (`JpaRepository`)
- `service/PratoService`

## Testes realizados
Os testes foram feitos no Postman, e os prints estão na pasta `evidencias/`: criação de 3 pratos (201),
listagem (200), busca por id (200), id inexistente (404), filtro por categoria (200), atualização (200),
remoção (204), busca após remoção (404), corpo inválido (400) e nome duplicado (409).

## Desafios extras
- Implementado: tratamento global de erros (`@ControllerAdvice`).
- Não implementados: `PATCH /pratos/{id}/valor` e `GET /pratos/calorias?max=`.

Autor: Daniel Victor Rodrigues Oliveira