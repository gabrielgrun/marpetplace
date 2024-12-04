# Índice / Table of Contents

- [Versão em Português](#versão-em-português-pt-br)
- [English Version](#english-version)

---

## **Versão em Português (pt-br)**
# Marketplace para Adoção de Animais

Este projeto é uma plataforma online para facilitar o processo de adoção de animais abandonados, 
conectando pessoas interessadas com ONGs e grupos que resgatam e cuidam desses animais.

Projeto criado para o meu TCC.

## Tecnologias Utilizadas
- Frontend: HTML, CSS e JS com Nginx para servir os arquivos estáticos
- Backend: Spring Boot (Java)
- Banco de Dados: PostgreSQL

## Como Rodar o Projeto
### Configuração das Variáveis de Ambiente
O projeto utiliza o arquivo .env para configurar algumas variáveis de ambiente importantes. Crie um arquivo .env na raiz do projeto e adicione as seguintes variáveis:

##### Configurações de Banco de Dados
- POSTGRES_USER=postgres
- POSTGRES_PASSWORD=123
- POSTGRES_DB=marpetplace

##### Configurações de E-mail
- MAIL_USERNAME=seu_email@gmail.com
- MAIL_PASSWORD=sua_senha_do_email

##### Configurações de JWT
- JWT_SECRET=seu_token_secreto

#### Descrição das Variáveis de Ambiente:
- POSTGRES_USER: Usuário do banco de dados PostgreSQL.
- POSTGRES_PASSWORD: Senha do usuário PostgreSQL.
- POSTGRES_DB: Nome do banco de dados a ser criado no PostgreSQL.
- MAIL_USERNAME: Seu e-mail (necessário para envio de e-mails, caso o projeto utilize esta funcionalidade).
- MAIL_PASSWORD: Senha do e-mail configurado.
- JWT_SECRET: Chave secreta para a geração de tokens JWT (utilizado para autenticação).

## Rodando o Projeto com Docker Compose
Com as variáveis de ambiente configuradas, você pode rodar o projeto com Docker Compose.

Execute o comando abaixo para iniciar os serviços do Docker:

`docker-compose up --build`

O sistema estará acessível em:

`localhost:3000`

---

## **English version**

# Animal Adoption Marketplace
This project is an online platform designed to facilitate the adoption of abandoned animals by connecting interested individuals with NGOs and groups that rescue and care for these animals.

Project created for my Bachelor's Thesis (TCC).

## Technologies Used
- Frontend: HTML, CSS, and JS with Nginx to serve static files
- Backend: Spring Boot (Java)
- Database: PostgreSQL

## How to Run the Project
### Environment Variables Configuration
The project uses a .env file to configure some important environment variables. Create a .env file at the root of the project and add the following variables:

##### Database Configuration
- POSTGRES_USER=postgres
- POSTGRES_PASSWORD=123
- POSTGRES_DB=marpetplace

##### Email Configuration
- MAIL_USERNAME=your_email@gmail.com
- MAIL_PASSWORD=your_email_password

##### JWT Configuration
- JWT_SECRET=your_secret_token

#### Description of Environment Variables:
- POSTGRES_USER: PostgreSQL database username.
- POSTGRES_PASSWORD: PostgreSQL username password.
- POSTGRES_DB: Name of the PostgreSQL database to be created.
- MAIL_USERNAME: Your email (needed for sending emails, in case the project uses this functionality).
- MAIL_PASSWORD: Password of the configured email.
- JWT_SECRET: Secret key for generating JWT tokens (used for authentication).

## Running the Project with Docker Compose
Once the environment variables are configured, you can run the project with Docker Compose.

Run the following command to start the Docker services:

`docker-compose up --build`

The system will be accessible at:

`localhost:3000`
