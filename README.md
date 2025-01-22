# Grocery List Manager

A Spring Boot application for managing grocery lists with user authentication and a modern UI built with Thymeleaf and Bootstrap.

## Features

- 🔐 User Authentication (Login/Register)
- 📝 CRUD Operations for Groceries
- 🎲 Random Grocery Generator
- 💫 Modern, Responsive UI
- ⚡ Real-time Feedback Messages
- 🔄 Auto-dismissing Notifications
- 🎨 Professional UI with Bootstrap 5

## Getting Started

### Default Credentials
- Username: `admin`
- Password: `admin`

### Prerequisites
- Java 17 or higher
- Maven
- H2 Database


The application will be available at `http://localhost:9999`

## API Reference

### Health Check
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/ping` | Server health check - returns "pong" |

### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/login` | Show login page |
| POST   | `/login` | Process login |
| GET    | `/register` | Show registration page |
| POST   | `/register` | Process registration |
| GET    | `/logout` | Logout user |

### Grocery Management Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/groceries/list` | View all groceries |
| GET    | `/groceries/showFormForAdd` | Show add form |
| GET    | `/groceries/showFormForUpdate` | Show edit form |
| POST   | `/groceries/save` | Save/Update grocery |
| DELETE | `/groceries/delete` | Delete grocery |
| DELETE | `/groceries/deleteAll` | Delete all groceries |
| GET    | `/groceries/createRandomGrocery` | Add random grocery |

## Project Structure

### Controllers
- `TestController`: Health check endpoint
- `GroceryController`: Handles grocery CRUD operations
- `LoginController`: Manages authentication and user registration

### Entity Models
- `User`: User account model with fields:
  - id (Long)
  - username (String, unique)
  - password (String)
  - isAdmin (boolean)

### Templates
- `login.html`: Login page with default credentials display
- `register.html`: User registration form
- `list-groceries.html`: Main grocery list view
- `grocery-form.html`: Form for adding/editing groceries

## Technologies Used

### Backend
- Spring Boot
- Spring MVC
- Spring Data JPA
- MySQL

### Frontend
- Thymeleaf
- Bootstrap 5
- Font Awesome
- HTML/CSS/JavaScript


