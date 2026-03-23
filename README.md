# Face Attendance System Backend

## Project Overview
Spring Boot + MyBatis backend for Face Recognition Attendance System.

## Features
- User Management (Login, Register, JWT)
- Employee Management (Add, List, Delete)
- Face Recognition Integration (Python Service)
- Attendance & Capture Recording
- Video Playback

## Prerequisites
- Java 25+
- MySQL 8.0+
- Maven 3.6+

## Configuration
Update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/face_attendance
    username: root
    password: root

face:
  python-api: http://localhost:5000
  upload-path: C:\graduate\shixi
```

## Running
1. Create database using `src/main/resources/sql/schema.sql`.
2. Run `mvn spring-boot:run`.

## API Documentation
See `api.md` for detailed API documentation.
