# Spring Security JWT Demo

A Spring Boot application that demonstrates stateless JWT authentication, database-backed users, role-based access control, permission-based access control, and method-level security.

## What the application does

The application stores users in an H2 database. A user registers or authenticates with a username and password. After a successful login, the application returns a JSON Web Token (JWT). Send that token in the `Authorization` header to access protected endpoints.

```text
Register or login -> JWT is returned -> JWT is sent with later requests
                                      -> JwtFilter validates the token
                                      -> Spring Security checks roles/permissions
```

## Requirements

- Java 21 or later
- No separate database installation is required; the project uses H2 in memory.

## Run the application

From the project root on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs at `http://localhost:8080` by default.

To run the tests:

```powershell
.\mvnw.cmd test
```

## Initial administrator account

At startup, `Usernames_Initilizer` creates this development-only administrator when it does not already exist:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | `ADMIN` |

The password is BCrypt-hashed before it is stored.

> The configured H2 database is in memory. Its users are removed when the application stops, so the development admin is created again on the next startup.

## API guide

### 1. Register a normal user

This endpoint is public and always creates a `USER` account. A public request cannot select an administrator role.

```http
POST /register/user
Content-Type: application/json

{
  "username": "shilpa",
  "password": "myPassword123"
}
```

Successful response:

```text
User registered successfully
```

### 2. Authenticate and receive a JWT

This endpoint is public.

```http
POST /authenticate
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

The response body is the JWT string. Copy it and use it in later requests:

```http
Authorization: Bearer <your-jwt-token>
```

Tokens expire after one hour.

### 3. Access an authenticated endpoint

```http
GET /api/name
Authorization: Bearer <your-jwt-token>
```

Any authenticated user can access this endpoint.

### 4. Access a permission-protected endpoint

```http
GET /api/city
Authorization: Bearer <your-jwt-token>
```

This requires the `read` authority. Both the `USER` and `ADMIN` roles have that permission.

### 5. Create another administrator

Only an existing administrator can create another administrator.

```http
POST /register/admin
Authorization: Bearer <admin-jwt-token>
Content-Type: application/json

{
  "username": "newadmin",
  "password": "securePassword123"
}
```

Calling this endpoint with a normal user token returns `403 Forbidden`.

## Access-control model

| Role | Spring role authority | Permissions |
| --- | --- | --- |
| `USER` | `ROLE_USER` | `read` |
| `ADMIN` | `ROLE_ADMIN` | `read`, `write`, `delete` |

`Users#getAuthorities()` converts the database role into Spring Security authorities. For example, an `ADMIN` user receives `ROLE_ADMIN`, `read`, `write`, and `delete`.

## Method-level security

Method-level security is enabled by `@EnableMethodSecurity` in `SecurityConfig`.

The project uses `@PreAuthorize` directly on controller methods:

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<String> registerAdmin(...) { ... }

@PreAuthorize("hasAuthority('read')")
public String getPlace() { ... }
```

The filter chain still permits only the public endpoints and requires authentication for every other request. The method annotation then applies the detailed role or permission check.

## Security flow

1. `SecurityConfig` permits `/authenticate` and `/register/user` without a token.
2. For other endpoints, `JwtFilter` runs before Spring's username/password filter.
3. `JwtFilter` reads `Authorization: Bearer <token>`, extracts the username, validates the token, and loads the current user from the database.
4. The filter stores that user's authorities in the Spring Security context.
5. URL security and `@PreAuthorize` rules decide whether the controller method can execute.

The application is stateless (`SessionCreationPolicy.STATELESS`), so the server does not store login sessions. CSRF protection is disabled because this is a token-based API rather than a browser session application.

## H2 console

For local development, open:

```text
http://localhost:8080/h2-console
```

Use these settings:

| Setting | Value |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:securitydb` |
| User Name | `sa` |
| Password | leave empty |

The H2 console is enabled only for development. Do not expose it in production.

## Common problems

### `403 Forbidden`

- Ensure the endpoint path is correct.
- Send a valid `Authorization: Bearer <token>` header for protected endpoints.
- Use an admin token for `POST /register/admin`.
- Obtain a new token if the existing token is older than one hour.

### `401 Unauthorized` or login failure

- Confirm the username exists.
- Confirm the password is correct.
- Do not save plaintext passwords directly in the database; passwords must be BCrypt hashes.

### Users disappear after restart

This is expected with `jdbc:h2:mem:securitydb`. Configure a file-based or external database for persistent data.

## Production notes

This project is a learning/demo application. Before deploying it, at minimum:

- Move the JWT secret out of `JwtUtil` and into a secure environment variable or secret manager.
- Use a persistent production database.
- Disable the H2 console.
- Add request validation and consistent JSON error responses.
- Handle malformed and expired JWTs with a controlled `401 Unauthorized` response.
- Use HTTPS.
