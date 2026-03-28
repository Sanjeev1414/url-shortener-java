# URL Shortener Application

A complete full-stack URL Shortener application built with Java, Spring Boot, MySQL, and JPA/Hibernate.

## Features

- REST API to shorten long URLs
- Generate unique short codes (6 characters)
- Redirect short URL to original URL
- Store data in MySQL database
- Track click count
- URL validation
- Handle duplicate URLs
- Custom short URL option
- Modern web interface

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Local Development Setup

1. Clone the repository
2. Update `application.properties` with your MySQL credentials
3. Create a MySQL database named `urlshortener`
4. Run the application: `mvn spring-boot:run`

## 🚀 Deployment to Render

### Step 1: Create Render Account
1. Go to [Render.com](https://render.com)
2. Sign up/Sign in with GitHub, GitLab, or Google

### Step 2: Deploy from GitHub
1. Click **"New"** → **"Blueprint"**
2. Connect your GitHub account
3. Select this repository: `url-shortener-java`
4. Render will automatically detect the `render.yaml` file
5. Click **"Apply"** to create the services

### Step 3: Services Created
Render will automatically create:
- **Web Service**: Your Spring Boot application
- **MySQL Database**: Persistent database service

### Step 4: Get Your Live URL
1. Once deployment completes (usually takes 5-10 minutes), go to your dashboard
2. Find your web service and click on it
3. Your live URL will be displayed (e.g., `https://url-shortener.onrender.com`)

## API Endpoints

### Shorten URL
- **POST** `/api/shorten`
- **Body**: `{"url": "https://example.com"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "https://your-domain.com/abc123", "shortCode": "abc123"}`

### Shorten URL with Custom Code
- **POST** `/api/shorten/custom`
- **Body**: `{"url": "https://example.com", "customCode": "mycode"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "https://your-domain.com/mycode", "shortCode": "mycode"}`

### Redirect to Original URL
- **GET** `/{shortCode}`
- Redirects to the original URL and increments click count

### Get URL Statistics
- **GET** `/api/stats/{shortCode}`
- **Response**: `{"originalUrl": "https://example.com", "shortCode": "abc123", "clickCount": 5, "createdAt": "2023-..."}`

### Shorten URL
- **POST** `/api/shorten`
- **Body**: `{"url": "https://example.com"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "https://your-domain.com/abc123", "shortCode": "abc123"}`

### Shorten URL with Custom Code
- **POST** `/api/shorten/custom`
- **Body**: `{"url": "https://example.com", "customCode": "mycode"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "https://your-domain.com/mycode", "shortCode": "mycode"}`

### Redirect to Original URL
- **GET** `/{shortCode}`
- Redirects to the original URL and increments click count

### Get URL Statistics
- **GET** `/api/stats/{shortCode}`
- **Response**: `{"originalUrl": "https://example.com", "shortCode": "abc123", "clickCount": 5, "createdAt": "2023-..."}`

## Sample API Requests (Postman)

### Shorten URL
```
Method: POST
URL: https://your-domain.com/api/shorten
Headers: Content-Type: application/json
Body:
{
    "url": "https://www.google.com"
}
```

### Shorten with Custom Code
```
Method: POST
URL: https://your-domain.com/api/shorten/custom
Headers: Content-Type: application/json
Body:
{
    "url": "https://www.google.com",
    "customCode": "google"
}
```

### Get Statistics
```
Method: GET
URL: https://your-domain.com/api/stats/google
```

### Test Redirect
```
Method: GET
URL: https://your-domain.com/google
```
(This will redirect to https://www.google.com)

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.1.0
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Build Tool**: Maven
- **Deployment**: Railway (Docker)

## Architecture

- **Controller Layer**: REST endpoints and web interface
- **Service Layer**: Business logic and URL shortening
- **Repository Layer**: Data access with JPA
- **Model Layer**: JPA entities for database mapping

## Environment Variables

The application uses the following environment variables:

- `PORT`: Server port (default: 8080)
- `DATABASE_URL`: MySQL JDBC connection URL
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password

Railway automatically provides these for deployed applications.

### Shorten URL
```
Method: POST
URL: http://localhost:8080/api/shorten
Headers: Content-Type: application/json
Body:
{
    "url": "https://www.google.com"
}
```

### Shorten with Custom Code
```
Method: POST
URL: http://localhost:8080/api/shorten/custom
Headers: Content-Type: application/json
Body:
{
    "url": "https://www.google.com",
    "customCode": "google"
}
```

### Get Statistics
```
Method: GET
URL: http://localhost:8080/api/stats/google
```

### Test Redirect
```
Method: GET
URL: http://localhost:8080/google
```
(This will redirect to https://www.google.com)