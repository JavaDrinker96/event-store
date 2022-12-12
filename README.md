# Event store API
This project is a CRUD Rest API for working with events. With it, you can perform simple operations to manage scheduled events. 

The following operations are available:
- Getting a specific event by id
- Registration (creation) of a new event
- Changing information about an existing event
- Deleting an event
- Getting a list of all events
- Getting a list of events according to the specified search criteria (by topic, planner, date, time)

## Built With
[![Java][Java-badge]][Java-url]
[![Spring-boot][Spring-boot-badge]][Spring-boot-url]
[![Postgres][Postgres-badge]][Postgres-url]
[![Maven][Maven-badge]][Maven-url]
[![Docker][Docker-badge]][Docker-url]
[![Swagger][Swagger-badge]][Swagger-url]
- Java 11
- Spring boot 2.6.0
- PostgreSQL
- Maven
- Docker and Docker-compose
- Spring fox

## Getting Started
### Prerequisites
First of all, you need to install and configure docker and docker-compose locally.
Follow [this link](https://docs.docker.com/get-docker/) to install docker and [this one](https://docs.docker.com/compose/install/) to install docker-compose.

### Installation
1. Clone the repo
   ```
   > git clone https://github.com/JavaDrinker96/event-store.git
   ```
2. Go to the project folder and configure .env file for your needs 
   - `API_SERVICE_PORT` - the port from which the api will be available on your local machine
   - `POSTGRES_DATABASE_PORT` - the port from which the database will be accessible on your local machine
   - `POSTGRES_DATABASE_NAME` - postgres database name
   - `POSTGRES_DATABASE_USERNAME` - name of the database user
   - `POSTGRES_DATABASE_PASSWORD` - password of the database user
   
3. Open the project folder using the terminal and run the command:
   ```
   > docker-compose up -d
   ```
## Using
   - Type the appropriate url in the browser:
   ```
   http://localhost:{API_SERVICE_PORT}/api/swagger-ui/
   ```
   There you will find the necessary information about how to interact with the api.

   - For run project use:
   ```
   > docker-compose up -d
   ```
   - For stop running use:
   ```
   > docker-compose down
   ```
[Java-badge]: https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white
[Java-url]: https://www.oracle.com/java/
[Postgres-badge]: https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white
[Postgres-url]: https://www.postgresql.org/
[Docker-badge]: https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
[Maven-badge]: https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white
[Maven-url]: https://maven.apache.org/
[Swagger-badge]: https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white
[Swagger-url]: https://swagger.io/
[Spring-boot-badge]: https://img.shields.io/badge/SPRING%20BOOT-6DB33F?style=for-the-badge&logo=spring&logoColor=white
[Spring-boot-url]: https://spring.io/projects/spring-boot
