# Microservices de Gestion des Commandes avec Kafka

![Architecture](docs/architecture.png)

Ce projet implémente une architecture microservices pour la gestion des commandes clients avec communication asynchrone via Apache Kafka.

## Fonctionnalités

### **Service des Commandes**
- API REST pour créer des commandes (`POST /commandes`)
- Validation des données de commande
- Publication d'événements Kafka `orders.created`
- Stockage des commandes en base de données

### **Service de Gestion des Stocks**
- Consommation des événements `orders.created`
- Vérification de la disponibilité des produits
- Mise à jour automatique des stocks
- API REST pour consulter les stocks (`GET /stocks`)

##  Installation avec Docker
- Docker 20.10+
- Docker Compose 2.0+
### Technologies

- **Backend** : Spring Boot 3.x, Java 21
- **Messagerie** : Apache Kafka
- **Base de données** : MySQL
- **Containerisation** : Docker, Docker Compose
###  Cloner le repository
git clone https://github.com/AttiaSabrine18/devAv-Microservices-Gestion-Commandes.gitcd devAv-Microservices-Gestion-Commandes
## Démarrer tous les services
bash
docker-compose up -d
Service Commandes : 	http://localhost:8080	
Service Stocks	: http://localhost:8081


## 📁 Structure du Projet
