# 🏗️ Task Collab App — Backend (Java Spring Boot)

Application web de gestion de tâches collaboratives inspirée de Trello/Notion.  
Ce backend RESTful est développé avec **Spring Boot** (Java 17+), **MySQL**, et sécurisé avec JWT.

---

## 🚀 Fonctionnalités principales

- Authentification sécurisée (JWT, BCrypt)
- Gestion des utilisateurs, boards, colonnes (ListColumn) et tâches (CRUD)
- Un système d’upload d’images lié aux tâches (PNG, JPG, WEBP)
- Suppression automatique des images lors de la suppression d’une tâche
- Gestion fine des droits (route protégée, email unique…)
- Architecture professionnelle : entités + DTO (pas d’exposition d’entités JPA brutes)
- Documentation API interactive : Swagger / OpenAPI

---

## ⚡️ Installation et démarrage rapide

### 1. Prérequis

- Java 17 ou 21+
- Maven
- MySQL (ex : WAMP, XAMPP, MAMP, ou MySQL “nu”)
- (Optionnel) VSCode, IntelliJ IDEA, Eclipse…

### 2. Clone & setup

```bash
git clone https://github.com/martav69/task-collab-app.git
cd task-collab-app/backend
```

### 3. Clone & setup

- Crée une base, par exemple task_manager
- Configure le fichier src/main/resources/application.properties :

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/task_manager?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=tonmdp
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
app.upload.dir=uploads
```

### 4. Clone & setup

```bash
# Depuis le dossier backend
mvn spring-boot:run
```

## 📚 Documentation interactive — Swagger
- Accès direct :
[Swagger](http://localhost:8080/swagger-ui.html).
> Après avoir lancé votre serveur

Tout est auto-documenté : endpoints, modèles DTO, tests live, etc.

## 🛡️ Sécurité
Mot de passe hashé (BCrypt, jamais stocké ou renvoyé en clair)

JWT (Token valable 24h, à fournir dans chaque requête sécurisée)

DTO only : seules les infos utiles au front sont exposées (jamais de password ou infos sensibles)

CORS activé (pour le front React/JS)

Swagger ouvert uniquement en dev

## 📦 Technos utilisées
- Java 17+
- Spring Boot 3+
  Spring Security
- JWT
- BCrypt
- Spring Data JPA (Hibernate)
- MySQL
- Swagger/OpenAPI (springdoc-openapi)
- Maven

## 📄 Structure du code

```bash
backend/
│
├── src/main/java/com/mv/backend/
│   ├── controller/      # Contrôleurs REST
│   ├── dto/             # DTO exposés au front
│   ├── entity/          # Entités JPA (modèles BDD)
│   ├── repository/      # Accès aux données (DAO)
│   ├── security/        # Auth, JWT, gestion utilisateur sécurisé
│   ├── config/          # Configurations diverses (CORS, Security, etc.)
│   └── exception/       # Exceptions personnalisées
│
├── src/main/resources/
│   └── application.properties
├── uploads/             # Stockage local des images
├── pom.xml
└── ...

```

## 👨‍💻 Développement & contribution
#### Réalisé par Martav 

> Issue, questions, suggestions : ouvrez une Issue GitHub ou contactez-moi

### 🌟 Licence
Projet open-source, servez-vous !
