# 🏘️ TCCImoveis - Backend

Este é o back-end da aplicação imobiliária **TCCImoveis**, desenvolvida como parte de um projeto acadêmico com foco em gerenciamento de imóveis, agendamento de visitas e geração de relatórios.  
A aplicação foi construída utilizando **Java 21** com o framework **Spring Boot 3.4.0**, seguindo boas práticas de desenvolvimento e arquitetura RESTful.

---

## 🚀 Tecnologias Utilizadas

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Spring Boot 3.4.0](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)

---

## 📦 Pré-requisitos

Antes de rodar a aplicação, você precisa ter:

- [Docker e Docker Compose instalados](https://www.docker.com/products/docker-desktop/)
- PostgreSQL rodando (localmente ou via container)
- Java 21 e Maven instalados (caso deseje rodar fora do Docker)

---

## 🐳 Docker

### 🔨 Build da imagem

```bash
docker build -t tccimoveis-backend .
