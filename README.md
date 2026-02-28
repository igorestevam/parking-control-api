# Parking Control API

> API RESTful desenvolvida para o gerenciamento e controle de vagas de estacionamento de condomínios.

Este projeto foi inicialmente inspirado no curso de Spring Boot da Michelli Brito (2022), porém foi **levemente modernizado e refatorado** para utilizar padrões mais recentes, incluindo Java 21, Spring Boot 4 e paginação global.

## Tecnologias e Ferramentas

O projeto foi construído utilizando o seguinte ecossistema:

* **Java 21**
* **Spring Boot 4.0.3** (WebMVC, Data JPA, Validation, Jackson Datatype)
* **PostgreSQL** (Banco de dados relacional)
* **Lombok** (Redução de boilerplate)
* **Maven** (Gerenciamento de dependências)

## Funcionalidades e Diferenciais Modernos

Além do CRUD (Create, Read, Update, Delete) tradicional, esta API conta com melhorias significativas de arquitetura:

* **Identificadores Únicos (UUID):** Substituição do ID sequencial tradicional por UUID, garantindo maior segurança e escalabilidade para a aplicação.
* **Datas Padronizadas em UTC (ISO 8601):** Configuração nativa utilizando `Jackson2ObjectMapperBuilderCustomizer` e a anotação `@JsonFormat` para garantir que todas as datas da API (`LocalDateTime`) sejam salvas e retornadas no formato universal terminando em `Z` (ex: `2026-02-28T00:57:31Z`), evitando problemas de fuso horário no Front-end.
* **Paginação Global Customizada:** Implementação de paginação via parâmetros na URL (`page`, `size`, `sort`), com os valores padrão e limites máximos de segurança devidamente centralizados no `application.properties`, mantendo os Controllers limpos.
* **Validação de Dados:** Uso rigoroso do Spring Boot Validation (Jakarta Bean Validation) nos DTOs para garantir a integridade dos dados antes de chegarem ao banco.

## Como Executar o Projeto

**1. Clonar repositório:**

    git clone https://github.com/seu-usuario/parking-control-api.git
  
**2. Configurar o banco de dados:**

Edite o arquivo `src/main/resources/application.properties` com suas credenciais do PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_control_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```
**3. Executar a aplicação:**

Você pode rodar o projeto pela sua IDE de preferência ou via terminal utilizando o Maven:

    mvn spring-boot:run

**4. Acesse a API:**

A aplicação rodará localmente em `http://localhost:8080`

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.
