# Desafio Técnico - Gestão de Tarefas

## Objetivo

Esta aplicação web foi desenvolvida com o objetivo é gerenciar tarefas, permitindo cadastro de usuários e manutenção de tarefas com filtragem e ordenação.

## Tecnologias Utilizadas

- **Backend:** Java 17+, Spring Boot 3+, Spring Data JPA, Docker
- **Banco de Dados:** PostgreSQL (ou H2 para testes)
- **Frontend:** React (ou Vue 3, Angular)

## Funcionalidades

### Cadastro de Usuários

- Registro de usuário com nome, e-mail e senha.

### Gerenciamento de Tarefas

- Criar, editar, excluir e listar tarefas.
- Cada tarefa possui:
  - Título
  - Descrição
  - Status (Pendente, Em Andamento, Concluído)
  - Data de vencimento
- Apenas o criador da tarefa pode editá-la ou excluí-la.

### Filtragem e Ordenação

- Filtrar tarefas por status.
- Ordenar por data de vencimento.

## Requisitos Não Funcionais

- Código seguindo boas práticas de desenvolvimento.
- Testes unitários para pelo menos uma camada do backend.
- Frontend responsivo.

## Como Executar o Projeto

### 1. Configuração do Backend

1. Certifique-se de ter o **Docker** instalado.
2. No terminal, navegue até o diretório do backend e execute:
   ```sh
   docker-compose up -d
   ```
   Isso iniciará um contêiner do PostgreSQL.
3. Abra o backend no IntelliJ IDEA, Eclipse ou outra IDE compatível.

### 2. Configuração do Frontend

1. Certifique-se de ter o **Node.js** instalado.
2. No terminal, navegue até o diretório do frontend e execute:
   ```sh
   npm install
   ```
   Para instalar as dependências.
3. Inicie o frontend:
   ```sh
   npm start
   ```
4. O aplicativo estará disponível em `http://localhost:3000/` (caso utilize React).

## Decisões Técnicas

- **Spring Boot:** Escolhido pela facilidade de desenvolvimento, escalabilidade e integração com Spring Data JPA.
- **PostgreSQL:** Banco de dados relacional robusto, utilizado para armazenar as tarefas e usuários.
- **Docker:** Facilita a configuração e execução do banco de dados.
- **React:** Escolhido pela popularidade e suporte da comunidade para desenvolvimento frontend moderno.


