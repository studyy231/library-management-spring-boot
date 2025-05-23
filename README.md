## Library Management System

A library book management web application I built for my Software Methodologies class

## What This Project Does
This app lets you manage a library's book collection through a web interface. You can add new books, search through the catalog, update book information, and remove books when needed. I built it with a proper database backend and included comprehensive testing.


## Assignment Requirements
- Spring Boot app with layered architecture (Controller → Service → Repository)
- H2 database for storing book data
- Full CRUD operations via REST APIs
- Unit tests for business logic and API endpoints 
- Run configurations for easy development workflow


## Tech Stack I Used
- Spring Boot 3.4.6 - Main framework 
- Java 17 - Programming language
- H2 Database - In-memory database for development
- JUnit 5 + Mockito - For testing
- Maven - Dependency management
- IntelliJ IDEA - Development environment


## Run Configurations
As required by the assignment, I created two .run configuration files:
-LibraryApplication.run.xml - Runs the Spring Boot application
-AllTests.run.xml - Runs all unit tests (13 tests total)

These make development much easier, just select the configuration from the dropdown and click the green play button.


## Getting Started
!!!!!!Running the App
1. Open the project in IntelliJ
2. Use the `LibraryApplication` run configuration
3. Click the green run button 
4. Go to `http://localhost:8081/api/books` to see it working
5. Access H2 database console at `http://localhost:8081/h2-console`
   - JDBC URL: `jdbc:h2:mem:library_db`
   - Username: `sa` 
   - Password: `password`

!!!!!!!Running Tests

1.Select the AllTests run configuration from the dropdown
2.Should see 13/13 tests pass

## API Reference

| Method | Endpoint | What it does |
|--------|----------|-------------|
| GET | `/api/books` | Shows all books |
| GET | `/api/books/{id}` | Gets one specific book |
| POST | `/api/books` | Adds a new book |
| PUT | `/api/books/{id}` | Updates book info |
| DELETE | `/api/books/{id}` | Removes a book |
| GET | `/api/books/search?q=spring` | Searches books by title/author |
| GET | `/api/books/available` | Shows only available books |


## Testing
I wrote 13 different tests covering:
- Service layer (7 tests): Business logic, error handling, edge cases
- Controller layer (5 tests): HTTP requests/responses, JSON handling
- Application context (1 test): Making sure Spring boots up correctly
All tests use Mockito to avoid hitting the real database during testing.


## Database Schema
The app uses one main table called `books`:

sql
CREATE TABLE books (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL, 
    isbn VARCHAR(255) UNIQUE NOT NULL,
    category VARCHAR(255),
    available BOOLEAN NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);


## Project Structure
src/
├── main/java/com/library/
│   ├── controller/     REST endpoints
│   ├── service/        Business logic  
│   ├── repository/     Database operations
│   ├── entity/         Data models
│   └── exception/      Error handling
└── test/java/com/library/
    ├── service/        Service tests
    └── controller/     Controller tests

    

## Challenges I Faced
1. Lombok issues - Had problems with auto-generated getters/setters, ended up writing them manually
2. Port conflicts - App wouldn't start on 8080, switched to 8081
3. Testing framework - @MockBean was deprecated, had to learn newer testing approaches
4. Git authentication - Needed to set up personal access tokens for GitHub



## Testing Results
Service Tests: 7/7 passed 
Controller Tests: 5/5 passed 
Application Context Test: 1/1 passed 
Total: 13/13 tests passed 

## course & student info
Course: Software Methodologies  
Student: Lea Akkaoui  
