# Controle Lanchonete

Sistema web de controle de pedidos para lanchonete, desenvolvido em **Spring Boot** com **Thymeleaf**, **HTMX**, **Tailwind CSS** e **PostgreSQL**, como Trabalho Final da disciplina de DAW I.

## Tecnologias

- Java 25 + Spring Boot 4.1
- Spring Data JPA / Hibernate
- Flyway (migrations de banco)
- Spring Security (login, papéis de usuário, HTTPS)
- Thymeleaf + HTMX (views)
- Tailwind CSS (estilo)
- JasperReports (relatório em PDF com subreport)
- PostgreSQL 18 (rodando em Docker)
- Maven

## Pré-requisitos

- JDK 25
- Maven 3.9+
- Docker (para o banco PostgreSQL)

## Configuração do banco de dados (PostgreSQL via Docker)

O projeto espera um PostgreSQL rodando em `localhost:5432`, com um banco `controlevacinacaoads` e um usuário `appspring`.

### 1. Suba um container PostgreSQL

Se ainda não tiver um container rodando, crie um:

```bash
docker run --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=12345 \
  -p 5432:5432 -d postgres:18
```

Se já existir um container PostgreSQL rodando (ex: outro projeto da disciplina), apenas garanta que ele está ativo:

```bash
docker start postgresql
```

### 2. Crie o usuário e o banco da aplicação

Entre no container e conecte como superusuário:

```bash
docker exec -it postgresql psql -U postgres
```

Dentro do `psql`, rode:

```sql
CREATE USER appspring WITH PASSWORD '12345';
CREATE DATABASE controlevacinacaoads OWNER appspring;
GRANT ALL PRIVILEGES ON DATABASE controlevacinacaoads TO appspring;
```

Saia com `\q`.

> As credenciais (`appspring` / `12345`) já estão configuradas em `src/main/resources/application-dev.properties`. Se quiser usar outras, ajuste esse arquivo.

### 3. As tabelas são criadas automaticamente

Ao iniciar a aplicação, o **Flyway** aplica todas as migrations de `src/main/resources/db/migration/` automaticamente — não é necessário criar tabelas manualmente.

## Criando o primeiro usuário (login)

Como o cadastro de usuários exige estar autenticado como `ADMIN`, é preciso inserir o primeiro usuário administrador manualmente, direto no banco, na primeira vez que o projeto for rodado em uma máquina nova.

Conecte no banco da aplicação:

```bash
docker exec -it postgresql psql -U postgres -d controlevacinacaoads
```

E rode:

```sql
INSERT INTO papel (nome) VALUES ('ROLE_ADMIN');
INSERT INTO papel (nome) VALUES ('ROLE_USUARIO');

INSERT INTO usuario (nome, email, nome_usuario, senha, data_nascimento, ativo)
VALUES ('Administrador', 'admin@lanchonete.com', 'admin', '{noop}admin123', '1990-01-01', true);

INSERT INTO usuario_papel (codigo_usuario, codigo_papel)
SELECT u.codigo, p.codigo
FROM usuario u, papel p
WHERE u.nome_usuario = 'admin' AND p.nome = 'ROLE_ADMIN';
```

Login padrão para testes:
- **Usuário:** `admin`
- **Senha:** `admin123`

> A senha usa o prefixo `{noop}`, que faz o Spring Security aceitá-la em texto puro — válido apenas para ambiente de desenvolvimento/teste.

Opcionalmente, crie também um segundo usuário com papel `ROLE_USUARIO` para testar o controle de acesso por perfil:

```sql
INSERT INTO usuario (nome, email, nome_usuario, senha, data_nascimento, ativo)
VALUES ('Atendente', 'atendente@lanchonete.com', 'atendente', '{noop}atendente123', '1995-05-05', true);

INSERT INTO usuario_papel (codigo_usuario, codigo_papel)
SELECT u.codigo, p.codigo
FROM usuario u, papel p
WHERE u.nome_usuario = 'atendente' AND p.nome = 'ROLE_USUARIO';
```

## Rodando o projeto

Pelo terminal, na raiz do projeto:

```bash
mvn spring-boot:run
```

A aplicação fica disponível em:
- HTTP: http://localhost:8080
- HTTPS: https://localhost:8443

Pela IDE (IntelliJ ou VSCode com extensão Java), basta rodar a classe `ControleVacinacaoApplication`.

## Funcionalidades

- **Clientes**: CRUD completo (cadastrar, alterar, pesquisar, remover)
- **Produtos**: CRUD completo, com preço
- **Pedidos**: cadastro de pedido vinculado a um cliente, com adição/remoção de itens (produto + quantidade) em tempo real via HTMX, exibindo total calculado
- **Usuários**: administrador pode cadastrar novos usuários com diferentes papéis (ADMIN / USUARIO)
- **Relatório**: relatório em PDF de todos os pedidos, com subreport listando os itens de cada pedido (relacionamento um-para-muitos)
- **Segurança**: login obrigatório, HTTPS, controle de acesso por papel (algumas rotas exigem ADMIN)

## Estrutura do projeto

```
src/main/java/web/controlevacinacao/
├── model/          # Entidades JPA (Cliente, Produto, Pedido, ItemDoPedido, Usuario, Papel)
├── dto/            # DTOs de entrada dos formulários
├── filter/         # Filtros de pesquisa
├── repository/     # Repositórios Spring Data JPA + queries customizadas
├── service/        # Camada de serviço / regras de negócio
├── controller/     # Controllers MVC
├── config/         # Configuração de segurança e web
├── report/         # Geração programática do relatório PDF (JasperReports)
└── validation/     # Validações customizadas (CPF único, etc.)

src/main/resources/
├── db/migration/    # Scripts Flyway (versionamento do banco)
├── templates/       # Views Thymeleaf
└── static/          # CSS (Tailwind) e JS
```
