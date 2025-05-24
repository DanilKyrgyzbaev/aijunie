This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here
  too.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack
channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.

## Running the Server with Docker

### Prerequisites

- Docker
- Docker Compose

### Getting Started

1. Clone the repository
2. Navigate to the project root directory
3. Run the following command to start the server:

```bash
docker-compose up
```

This will:
- Build the Ktor backend application
- Start a PostgreSQL database
- Connect the backend to the database
- Expose the backend on port 8080

### Environment Variables

The following environment variables can be configured in the `docker-compose.yml` file:

#### Backend Service
- `PORT`: The port on which the backend will run (default: 8080)
- `JWT_SECRET`: Secret key for JWT token generation and validation
- `DATABASE_URL`: JDBC URL for the PostgreSQL database
- `DATABASE_USERNAME`: Username for the PostgreSQL database
- `DATABASE_PASSWORD`: Password for the PostgreSQL database

#### Database Service
- `POSTGRES_DB`: Name of the PostgreSQL database
- `POSTGRES_USER`: Username for the PostgreSQL database
- `POSTGRES_PASSWORD`: Password for the PostgreSQL database

### API Access

Once the application is running, you can access the API at:

```
http://localhost:8080
```
