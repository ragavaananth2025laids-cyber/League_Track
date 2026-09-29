# League Track REST API

Spring Boot REST API for an intramural sports tournament. It supports team registration, single round-robin fixture generation, match result recording, and standings.

## Packages

Only these application packages are used:

- `controller`
- `model`
- `repository`
- `service`

## Requirements

- Java 17+
- IntelliJ IDEA
- Maven (or IntelliJ's Maven integration)
- MySQL Server + MySQL Workbench
- Postman

## 1. Create the database

In MySQL Workbench:

```sql
CREATE DATABASE league_track;
```

Do not create the tables manually. JPA/Hibernate creates/updates them.

## 2. Set the MySQL password

Open `src/main/resources/application.properties`.

The project uses:

```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}
```

If you do not want to configure an environment variable, replace the empty password with your MySQL password, for example:

```properties
spring.datasource.password=your_mysql_password
```

Do not commit a real password to a public repository.

## 3. Open in IntelliJ

Open the `League_Track` folder or its `pom.xml` in IntelliJ.

Wait for Maven to download dependencies. If IntelliJ asks, click **Load Maven Changes**.

Run:

`src/main/java/com/example/league_track/LeagueTrackApplication.java`

The server runs on:

`http://localhost:8080`

Swagger UI:

`http://localhost:8080/swagger-ui.html`

## 4. API flow in Postman

### Register teams

`POST http://localhost:8080/api/teams`

Body → raw → JSON:

```json
{
  "name": "Team A"
}
```

Repeat for Team B, Team C, Team D.

### View teams

`GET http://localhost:8080/api/teams`

### Generate fixture

`POST http://localhost:8080/api/fixtures/generate`

The generator creates a single round-robin schedule. Every team plays every other team once. Odd numbers of teams are supported using a bye slot.

### View fixtures

`GET http://localhost:8080/api/fixtures`

### View matches in a round

`GET http://localhost:8080/api/fixtures/{fixtureId}/matches`

### View all matches

`GET http://localhost:8080/api/matches`

### Record a result

`POST http://localhost:8080/api/matches/{matchId}/result?homeScore=3&awayScore=1`

Default scoring:

- Win = 3 points
- Draw = 1 point
- Loss = 0 points

The scoring values can be changed in `application.properties`.

A recorded match cannot be recorded again, preventing duplicate points.

### View standings

`GET http://localhost:8080/api/standings`

Standings are ordered by points descending, then wins descending.

## MySQL tables

After startup, Hibernate creates/updates these tables:

- `teams`
- `fixtures`
- `matches`
- `standings`

Useful Workbench checks:

```sql
USE league_track;
SHOW TABLES;
SELECT * FROM teams;
SELECT * FROM fixtures;
SELECT * FROM matches;
SELECT * FROM standings;
```
