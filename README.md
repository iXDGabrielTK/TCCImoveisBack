# 🏘️ TCCImoveis - Back-end

![Java](https://img.shields.io/badge/Java-21-blue?style=flat&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?style=flat&logo=spring)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

Este repositório contém o **back-end** da aplicação **Sistema de Administração de Imóveis**, desenvolvido como parte do
Trabalho de Conclusão de Curso em Análise e Desenvolvimento de Sistemas (UMFG).

🔗 **Repositório GitHub:** [TCCImoveisBack](https://github.com/iXDGabrielTK/TCCImoveisBack)

---

## 🧩 Tecnologias Utilizadas

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Spring Boot 3.4.0](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)
- [Flyway](https://flywaydb.org/) para versionamento do banco
- [JWT](https://jwt.io/) para autenticação segura
- [SpringDoc OpenAPI](https://springdoc.org/) para documentação da API
- [JasperReports](https://community.jaspersoft.com/project/jasperreports-library) para geração de relatórios PDF

---

## 🚀 Funcionalidades Principais

🔐 Autenticação segura com JWT (Access + Refresh)  
🧑‍💼 Cadastro de usuários com herança (`Visitante`, `Funcionário`, `Corretor`)  
🏘️ Gerenciamento de imóveis com endereço, status e fotos  
📅 Agendamento de visitas com controle de disponibilidade  
📄 Geração de relatórios PDF (usuários, visitas, vistorias)  
📊 Logging de acessos com hora e ação  
📦 Contêiner Docker pronto para deploy

> 📡 Esta API é consumida pelo front-end React do projeto TCCImoveis.

---

## 📂 Estrutura do Projeto

```
📁 src
┣ 📁 config        # Configurações de segurança, CORS, JWT
┣ 📁 controller    # Endpoints REST
┣ 📁 dto           # Data Transfer Objects
┣ 📁 entity        # Entidades JPA
┣ 📁 repository    # Interfaces com o banco
┣ 📁 service       # Lógica de negócio
┣ 📁 util          # Utils e helpers
┗ 📁 reports       # Relatórios Jasper (.jrxml/.jasper)
```

---

## 📦 Como Rodar Localmente

### 🔧 Pré-requisitos

- Java 21+
- Maven
- PostgreSQL
- Docker (opcional)

### 📁 Clonando o Projeto

```bash
git clone https://github.com/iXDGabrielTK/TCCImoveisBack.git
cd TCCImoveisBack
```

### 🔨 Compilando com Maven

```bash
./mvnw clean package -DskipTests
```

---

## 🐳 Docker

### 🏗️ Build da Imagem

```bash
docker build -t tccimoveis-back .
```

### 🚀 Rodando o Container

```bash
docker run -p 8080:8080 --env-file .env tccimoveis-back
```

> Certifique-se de que o PostgreSQL esteja rodando em `localhost:5432`, ou modifique `DB_URL`.

---

## ⚙️ Arquivo .env de Exemplo

```env
DB_URL=jdbc:postgresql://localhost:5432/seu_banco
DB_USERNAME=seu_usuario
DB_PASSWORD=sua_senha

SERVER_PORT=8080
HIBERNATE_DDL_AUTO=validate
SPRING_PROFILES_ACTIVE=secure

# Autenticação JWT
# Dica: o segredo deve ter no mínimo 64 caracteres
JWT_SECRET=troque-por-uma-senha-secreta-com-no-minimo-64-caracteres
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=86400000

REPORTS_PATH=src/main/resources/reports/
```

---

## 🧪 Rodando os Testes

```bash
./mvnw test
```

Os testes utilizam H2 Database e `@SpringBootTest`, com foco em controladores, serviços e segurança.

---

## 🔗 Endpoints Úteis

- `POST /usuarios/login`: Autenticação
- `POST /usuarios`: Cadastro de usuário
- `GET /imoveis`: Listar imóveis
- `POST /agendamentos`: Agendar visita
- `GET /relatorios/usuarios`: Relatório de acessos
- `GET /relatorios/vistorias`: Relatório de vistorias
- `GET /relatorios/agendamentos`: Relatório de visitas

---

## 📌 Sobre o Projeto

Este projeto é parte do Trabalho de Conclusão de Curso em Análise e Desenvolvimento de Sistemas da UMFG.  
Sistema desenvolvido para a empresa **Bemco Administradora de Bens**, com objetivo de digitalizar o gerenciamento de
imóveis, visitas e interações com clientes.

Acadêmicos responsáveis:

- 👤 Gabriel Ferrari Tanaka – [gabrielferraritanaka@gmail.com](mailto:gabrielferraritanaka@gmail.com)
- 👤 Caio Fabricio Dantas Ribeiro – [caaiofabricio07@gmail.com](mailto:caaiofabricio07@gmail.com)

---

## 📃 Licença

Este projeto é de uso exclusivo acadêmico, desenvolvido como parte de um Trabalho de Conclusão de Curso.  
Não está autorizado o uso comercial ou redistribuição sem autorização prévia dos autores.
---

## 💬 Contribuindo

Contribuições são bem-vindas!  
Para bugs, ideias ou sugestões, abra uma issue ou envie um PR.
