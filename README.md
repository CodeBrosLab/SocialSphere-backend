# SocialSphere 🌐

SocialSphere is a full-stack social media web application. It leverages Java-based technologies to deliver secure, scalable, and efficient social interaction functionality. The application uses Spring Boot, Spring JPA, and Spring Security for backend development, PostgreSQL as the database, Docker for containerization, Docker Compose for orchestration, and Fly.io for cloud-based deployment.

## Tech Stack

- **Backend Framework:** Spring Boot
- **Database:** PostgreSQL (managed with Spring Data JPA)
- **Security Framework:** Spring Security (JWT-based authentication and role-based authorization)
- **Containerization:** Docker, Docker Compose
- **Cloud Platform:** Fly.io (container-based hosting)

---

## Key Features

### Authentication & Security
- JWT Authentication with secure signup and login mechanisms.
- Password encryption using Spring Security.
- Role-based access control (e.g., User, Admin).

### Social Interaction
- User profiles creation and management.
- Posting, editing, and deleting posts.
- Commenting on posts.
- Follow and unfollow users functionality.

### Data Management
- Data persistence with PostgreSQL.
- Spring Data JPA for efficient ORM (Object-Relational Mapping).

### Containerization & Deployment
- Dockerfile provided for creating containerized builds.
- Docker Compose file included for local setup and orchestration.
- Deployed to Fly.io for scalable and robust hosting.

---

## Prerequisites

- **Java 17+**
  - [Download Java 17](https://www.oracle.com/java/technologies/downloads/)
- **Maven**
  - [Download Maven](https://maven.apache.org/download.cgi)
- **Docker & Docker Compose**
  - [Install Docker](https://docs.docker.com/get-docker/)
  - [Install Docker Compose](https://docs.docker.com/compose/install/)

---

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-username/socialsphere.git
cd socialsphere
```

### Run Locally with Docker Compose

```bash
mvn clean package -DskipTests
```
### Start the services

```bash
docker-compose up -d
```
### Your app should be accessible at:

```
http://localhost:8080
```

## Security
SocialSphere implements the following security measures:

- JWT tokens for stateless authentication.
- Passwords stored securely using hashing
- Role-based access control to ensure proper authorization of user actions.

### Contributors

- [Dimitris Sparagis](https://github.com/dimsparagis0210)
- [Thanos Moschou](https://github.com/thanosmoschou)
- [Andreas Hadjiantonis](https://github.com/ahadjiantonis-dev)





