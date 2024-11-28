# 🏥 Waiting Room Management API

Une API REST moderne pour la gestion intelligente des salles d'attente, construite avec **Spring Boot 3** et *
*PostgreSQL**.

---

## 🌟 Fonctionnalités

- 📋 **Gestion des visiteurs et des salles d'attente**
- 🧮 **Algorithmes de planification multiples : FIFO, Priority, Shortest Processing Time**
- ⏱️ **Suivi des statuts des visites**
- 📊 **Statistiques détaillées par salle d'attente**
- 📖 **Documentation API avec Swagger/OpenAPI**

---

## 🚀 Démarrage Rapide

### Prérequis

- ☕ **Java 17+**
- 🐳 **Docker & Docker Compose**
- 🛠️ **Maven 3.8+**

### Installation

1. **Cloner le repository**

   ```bash
   git clone https://github.com/yourusername/waiting-room-management.git
   cd waiting-room-management
   ```

2. **Construire le projet**

   ```bash
   mvn clean package -DskipTests
   ```

3. **Lancer avec Docker Compose**

   ```bash
   docker-compose up -d
   ```

    - L'API sera accessible à : [http://localhost:8080](http://localhost:8080)
    - Swagger UI : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🛠️ Stack Technique

- **Backend** : Spring Boot 3.3.5
- **Base de données** : PostgreSQL 15
- **Documentation** : OpenAPI 3.0
- **Tests** : JUnit 5, Testcontainers
- **Outils** : Lombok, MapStruct
- **Containerisation** : Docker

---

## 📚 Structure du Projet

```plaintext
src/
├── main/
│   ├── java/ma/nabil/WRM/
│   │   ├── controller/     # REST Controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entités JPA
│   │   ├── enums/          # Énumérations
│   │   ├── exception/      # Gestion des exceptions
│   │   ├── mapper/         # MapStruct Mappers
│   │   ├── repository/     # Repositories JPA
│   │   └── service/        # Logique métier
│   └── resources/
│       └── application.yml
```

---

## 🔄 Workflow des Visites

1. **WAITING** → État initial
2. **IN_PROGRESS** → Visite en cours
3. **FINISHED** → Visite terminée
4. **CANCELLED** → Visite annulée

---

## 🎯 Endpoints API Principaux

### 📌 Visiteurs

| Méthode | Endpoint             | Description                                       |
|---------|----------------------|---------------------------------------------------|
| POST    | /api/visitors        | Créer un nouveau visiteur                         |
| GET     | /api/visitors/{id}   | Obtenir un visiteur par ID                        |
| GET     | /api/visitors        | Liste paginée des visiteurs                       |
| PUT     | /api/visitors/{id}   | Mettre à jour un visiteur                         |
| DELETE  | /api/visitors/{id}   | Supprimer un visiteur                             |
| GET     | /api/visitors/exists | Vérifier l'existence d'un visiteur par nom/prénom |

### 📌 Salles d'Attente

| Méthode | Endpoint                          | Description                            |
|---------|-----------------------------------|----------------------------------------|
| POST    | /api/waiting-rooms                | Créer une nouvelle salle d'attente     |
| GET     | /api/waiting-rooms/{id}           | Obtenir une salle par ID               |
| GET     | /api/waiting-rooms                | Liste paginée des salles               |
| PUT     | /api/waiting-rooms/{id}           | Mettre à jour une salle                |
| DELETE  | /api/waiting-rooms/{id}           | Supprimer une salle                    |
| PUT     | /api/waiting-rooms/{id}/algorithm | Modifier l'algorithme de planification |
| PUT     | /api/waiting-rooms/{id}/work-mode | Modifier le mode de travail            |
| GET     | /api/waiting-rooms/{id}/stats     | Obtenir les statistiques d'une salle   |

### 📌 Visites

| Méthode | Endpoint                                              | Description                     |
|---------|-------------------------------------------------------|---------------------------------|
| POST    | /api/visits                                           | Créer une nouvelle visite       |
| GET     | /api/visits/{visitorId}/{waitingRoomId}               | Obtenir une visite spécifique   |
| GET     | /api/visits/waiting-room/{waitingRoomId}              | Liste des visites d'une salle   |
| PUT     | /api/visits/{visitorId}/{waitingRoomId}               | Mettre à jour une visite        |
| DELETE  | /api/visits/{visitorId}/{waitingRoomId}               | Supprimer une visite            |
| PUT     | /api/visits/{visitorId}/{waitingRoomId}/status        | Modifier le statut d'une visite |
| POST    | /api/visits/waiting-room/{waitingRoomId}/process-next | Traiter la prochaine visite     |

---

## 🔧 Configuration

### Application Properties

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<HOST>:<PORT>/<DATABASE_NAME>
    username: <USERNAME>
    password: <PASSWORD>
  jpa:
    hibernate:
      ddl-auto: update
server:
  port: 8080
```

---

## 🤝 Contribution

1. **Fork** le projet
2. **Créer une branche** (`git checkout -b feature/AmazingFeature`)
3. **Commit** les changements (`git commit -m 'Add AmazingFeature'`)
4. **Push** vers la branche (`git push origin feature/AmazingFeature`)
5. **Ouvrir une Pull Request**

---

## 👥 Auteurs

- **Nabil Ettihadi** - *Travail initial* - [GitHub](https://github.com/nabilettihadi)