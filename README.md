# URL Shortener Application

A complete URL Shortener application built with Java, Spring Boot, MySQL, and JPA/Hibernate.

## Features

- REST API to shorten long URLs
- Generate unique short codes (6 characters)
- Redirect short URL to original URL
- Store data in MySQL database
- Track click count
- URL validation
- Handle duplicate URLs
- Custom short URL option

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository
2. Update `application.properties` with your MySQL credentials
3. Create a MySQL database named `urlshortener`
4. Run the application: `mvn spring-boot:run`

## API Endpoints

### Shorten URL
- **POST** `/api/shorten`
- **Body**: `{"url": "https://example.com"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "http://localhost:8080/abc123", "shortCode": "abc123"}`

### Shorten URL with Custom Code
- **POST** `/api/shorten/custom`
- **Body**: `{"url": "https://example.com", "customCode": "mycode"}`
- **Response**: `{"originalUrl": "https://example.com", "shortUrl": "http://localhost:8080/mycode", "shortCode": "mycode"}`

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