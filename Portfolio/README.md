# Portfolio

A full-stack personal portfolio organized into separate frontend, backend, and documentation directories.

## 📁 Project Structure

```text
Portfolio/
├── docs/
├── server/
└── web/
```

### `docs/`

Hoppscotch collection for testing the API.

```text
docs/
└── Portfolio.json
```

Import it into Hoppscotch, then set two environment variables:

- `BASE_URL` → `http://localhost:3333/api/`
- `TOKEN` → the `accessToken` returned by `account/login/`

### `server/`

Kotlin + Spring Boot REST API (PostgreSQL, JWT auth).

```text
server/src/main/kotlin/com/ralphmarondev/portfolio/
├── config/        security, JWT, CORS, admin seeding
├── controller/    account, project, skill, message
├── dto/
├── entity/
├── exception/
├── mapper/
├── repository/
└── service/
```

#### Environment Variables

```text
JDBC_DATABASE_URL=jdbc:postgresql://localhost:5432/portfolio
JDBC_DATABASE_USERNAME=
JDBC_DATABASE_PASSWORD=
ADMIN_PASSWORD=            # required once, to create the admin account
ADMIN_USERNAME=admin       # optional
JWT_SECRET=                # 32+ characters, keeps tokens valid across restarts
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

```bash
cd server
./gradlew bootRun        # Windows: gradlew.bat bootRun
```

API base: `http://localhost:3333/api/`

| Endpoint | Access |
| --- | --- |
| `GET project/`, `GET skill/` | public |
| `POST message/` (contact form) | public |
| `POST account/login/` | public |
| `GET account/me/` | logged in |
| project / skill writes, `GET message/` | admin |

### `web/`

Vue 3 + TypeScript + Vite + Tailwind CSS 4 + Pinia.

```bash
cd web
cp .env.example .env
npm install
npm run dev              # http://localhost:5173
```

Edit your name, role, bio and links in `web/src/config/site.ts`.
The admin dashboard is at `/login` → `/dashboard` (manage projects, skills, messages).

## 🚀 Getting Started

1. Create a PostgreSQL database and set the server environment variables.
2. `cd server && ./gradlew bootRun`
3. `cd web && npm install && npm run dev`
4. Log in at `/login`, add projects and skills from the dashboard (or use the Hoppscotch collection).
