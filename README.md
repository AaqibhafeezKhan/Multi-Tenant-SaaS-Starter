# SaaS Core Starter

## Prerequisites
- Java 21
- Maven 3.9+

## Running Locally
The application is entirely self-sustaining and uses an embedded H2 database. There is no need to configure external databases.

Run the application:
```shell
mvn spring-boot:run
```

Once started, navigate to `http://localhost:8080/` in your browser to view the interactive API Hub display layer.

## Environment Variables
| Variable | Description | Default |
|---|---|---|
| SPRING_DATASOURCE_URL | Database JDBC URL | jdbc:h2:mem:saas_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL |
| SPRING_DATASOURCE_USERNAME | Database Username | saas_admin |
| SPRING_DATASOURCE_PASSWORD | Database Password | saas_password |

## H2 Database Console
The H2 administration console is enabled at `http://localhost:8080/h2-console`. 
- **JDBC URL**: Configure it to match your `SPRING_DATASOURCE_URL` default.
- **User Name**: saas_admin
- **Password**: saas_password

## API Endpoints
| Method | Path | Auth Required | Description |
|---|---|---|---|
| POST | /auth/register | No | Register a new user for a tenant (Requires X-Tenant-ID header) |
| POST | /auth/login | No | Login and receive JWT (Requires X-Tenant-ID header) |
| GET | /api/projects | Yes | Get all projects for the current tenant |
| GET | /api/projects/{id} | Yes | Get a specific project by ID |
| POST | /api/projects | Yes | Create a new project |
| PUT | /api/projects/{id} | Yes | Update an existing project |
| DELETE | /api/projects/{id} | Yes | Delete a project |
| GET | /actuator/health | No | Application health check |
