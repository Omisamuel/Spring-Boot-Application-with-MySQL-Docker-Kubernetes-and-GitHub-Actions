# **Spring Boot Application with MySQL, Docker, Kubernetes, and GitHub Actions - Full Documentation**



### Introduction

This documentation provides a comprehensive guide to setting up a Spring Boot application with MySQL and PostgreSQL databases, integrated with CI/CD tools like SonarQube, Nexus, and Jenkins using Docker, Docker Compose, and Kubernetes. The guide also covers integrating GitHub Actions for continuous integration and deployment (CI/CD).

### Table of Contents

1. Prerequisites
2. Project Structure
3. Setting Up the Spring Boot Application
    * Application Overview
    * Application Configuration (application.properties)
4. Dockerization
    * Creating a Dockerfile
    * Building and Running the Docker Image
5. Setting Up Database and CI/CD Tools with Docker Compose
    * Docker Compose Configuration
    * Running the Application and CI/CD Tools with Docker Compose
6. Deploying to Kubernetes
    * Kubernetes Deployment YAML
    * Kubernetes Service YAML
    * Setting Up Kubernetes Secrets
    * mySQL YAML
    * mySQL Service YAML
7. Integrating GitHub Actions for CI/CD
    * GitHub Actions Workflow Configuration
    * Secrets Configuration in GitHub
8. Troubleshooting Common Issues
9. Conclusion




## Prerequisites

Before you begin, ensure you have the following installed:

* Java Development Kit (JDK) 11+
* Maven
* Docker
* Docker Compose
* Kubernetes (Minikube)
* Kubectl
* GitHub Actions (Configured via your GitHub repository)
* Git


### Project Structure

Here’s the structure of the project:


```inventory-management-system/
inventory-management-system/
├── .github/
│   └── workflows/
│       └── ci-cd.yml
├── Dockerfile
├── README.md
├── docker-compose.yml
├── kubernetes/
│   ├── deployment.yaml
│   └── service.yaml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── company/
│   │   │           └── inventory/
│   │   │               ├── InventoryApplication.java
│   │   │               ├── controllers/
│   │   │               │   ├── InventoryController.java
│   │   │               │   └── IndexController.java
│   │   │               ├── models/
│   │   │               │   └── Product.java
│   │   │               ├── repositories/
│   │   │               │   └── ProductRepository.java
│   │   │               └── services/
│   │   │                   └── InventoryService.java
│   │   └── resources/
│   │       └── application.properties
└── target/

```



### Setting Up the Spring Boot Application

#### Application Overview

This application is a simple inventory management system where products can be added, updated, deleted, and queried. The application uses Spring Boot, Spring Data JPA, and MySQL.

#### Application Configuration (application.properties)

Configure the Spring Boot application to connect to MySQL:

```server.port=8080

spring.datasource.url=jdbc:mysql://mysql-service:3306/inventory_db
spring.datasource.username=inventory_user
spring.datasource.password=password
   
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```



### Dockerization

#### Creating a Dockerfile

Create a Dockerfile to containerize the Spring Boot application:


````# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/inventory-management-system.jar /app/inventory-management-system.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "inventory-management-system.jar"]
````

### Building and Running the Docker Image

**Build the Docker Image:**
````
mvn clean package
docker build -t company/inventory-management-system:latest .
```` 
**Run the Docker Image:**

````
docker run -p 8080:8080 company/inventory-management-system:latest
````

## Setting Up MySQL with Docker Compose

**Docker Compose Configuration**

Create a docker-compose.yml file to define the MySQL service and link it with the Spring Boot application.

here is the file [link](https://github.com/omilabs/Java-based-DevOps-Project-E-commerce-Application/blob/main/docker-compose.yml)


## Running the Application with Docker Compose

**Run the application:**
````
docker-compose up -d
````


### Deploying to Kubernetes

#### Kubernetes Deployment YAML

Create a deployment.yaml file in the kubernetes/ directory:
here is the file [link](https://github.com/omilabs/Java-based-DevOps-Project-E-commerce-Application/blob/main/docker-compose.yml)

````
apiVersion: apps/v1
kind: Deployment
metadata:
  name: inventory-management-system
spec:
  replicas: 3
  selector:
    matchLabels:
      app: inventory-management-system
  template:
    metadata:
      labels:
        app: inventory-management-system
    spec:
      containers:
      - name: inventory-management-system
        image: company/inventory-management-system:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_DATASOURCE_URL
          value: "jdbc:mysql://mysql-service:3306/inventory_db"
        - name: SPRING_DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: username
        - name: SPRING_DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: password
````

## Kubernetes Service YAML

Create a service.yaml file in the kubernetes/ directory:

````
apiVersion: v1
kind: Service
metadata:
  name: inventory-management-service
spec:
  type: LoadBalancer
  ports:
  - port: 80
    targetPort: 8080
  selector:
    app: inventory-management-system
````

### Setting Up Kubernetes Secrets

Create a Kubernetes secret to store the MySQL credentials:

`````
kubectl create secret generic mysql-secret \
  --from-literal=username=inventory_user \
  --from-literal=password=password
`````

### Deploy to Kubernetes

Apply the deployment and service YAML files:

````
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
````


### Integrating GitHub Actions for CI/CD

#### GitHub Actions Workflow Configuration
Create a GitHub Actions workflow in .github/workflows/**ci-cd.yml**:

[link](https://github.com/omilabs/Java-based-DevOps-Project-E-commerce-Application/blob/main/.github/workflows/ci-cd-pipeline.yml) to code



#### Secrets Configuration in GitHub

Go to your GitHub repository.
Click on Settings.
Select Secrets and variables -> Actions.
Add the following secrets:

    DOCKER_USERNAME: Your Docker Hub username.
    DOCKER_PASSWORD: Your Docker Hub password.
    KUBECONFIG: The kubeconfig file content for accessing your Kubernetes cluster.


# Local Testing
````
mvn clean install
````

````
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dproperty1=overridden" -X

````

### Troubleshooting Common Issues

* #### Communications Link Failure
  This usually indicates a problem with the connection to the MySQL server. Ensure that the MySQL service is running and accessible from your application container.

* #### Unknown MySQL Server Host
  This error occurs if the application cannot resolve the MySQL service hostname. Ensure that the services are correctly networked in Docker Compose or Kubernetes.
* #### HikariPool Initialization Failure
  This could be caused by incorrect database credentials or a misconfigured application.properties file. Verify that the Spring Boot application is correctly configured to connect to the database.

* #### Ports Already in Use
  If you encounter errors stating that a port is already in use, either choose a different port or stop the service that is occupying the port.


### Conclusion

This documentation provides a complete guide to setting up and running a Spring Boot application with MySQL and PostgreSQL using Docker, Docker Compose, and Kubernetes. It also covers integrating CI/CD tools like SonarQube, Nexus, and Jenkins, and automating the process using GitHub Actions.

By following these steps, you should have a fully functioning environment capable of deploying and managing a Spring Boot application in a production-like environment. If you encounter any issues or need further assistance, please refer to the troubleshooting section or consult the relevant documentation for each tool.