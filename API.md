# 📡 API Documentation — Scena MVP

Base URL: `http://localhost:8080`

Documentació interactiva (Swagger UI): [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🔐 Autenticació

L'API utilitza **JWT (JSON Web Token)**. Els endpoints protegits requereixen la capçalera:

```
Authorization: Bearer <token>
```

El token s'obté fent login a `POST /api/auth/login`.

---

## 📋 Endpoints

### 1. Autenticació (`/api/auth`)

#### `POST /api/auth/register`

Registre d'un nou usuari.

| Camp | Detall |
|---|---|
| **Accés** | Públic |
| **Request Body** | JSON |
| **Response** | `201 Created` |

**Request:**
```json
{
  "name": "Luka",
  "email": "luka@scena.com",
  "password": "123456"
}
```

**Responses:**
| Codi | Descripció |
|---|---|
| `201` | Usuari registrat correctament |
| `400` | Dades invàlides |
| `409` | Email ja registrat |

---

#### `POST /api/auth/login`

Login amb credencials. Retorna JWT token.

| Camp | Detall |
|---|---|
| **Accés** | Públic |
| **Request Body** | JSON |
| **Response** | `200 OK` |

**Request:**
```json
{
  "email": "luka@scena.com",
  "password": "123456"
}
```

**Response (200):**
```json
{
  "message": "Login exitoso",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "name": "Luka",
  "email": "luka@scena.com",
  "id": 1
}
```

**Errors:**
| Codi | Descripció |
|---|---|
| `400` | Dades invàlides |
| `401` | Credencials incorrectes |

---

### 2. Esdeveniments (`/api/events`)

#### `GET /api/events`

Llistar tots els esdeveniments. Suporta filtres opcionals.

| Camp | Detall |
|---|---|
| **Accés** | Públic |
| **Query Params** | `category`, `date`, `search` (tots opcionals) |

**Exemples:**
```
GET /api/events
GET /api/events?category=MUSICA
GET /api/events?date=2026-03-01
GET /api/events?search=jazz
GET /api/events?category=ESPORT&date=2026-03-15
```

**Response (200):** Array d'esdeveniments.

---

#### `GET /api/events/{id}`

Obtenir un esdeveniment per ID.

| Camp | Detall |
|---|---|
| **Accés** | Públic |

**Errors:**
| Codi | Descripció |
|---|---|
| `404` | Esdeveniment no trobat |

---

#### `POST /api/events`

Crear un nou esdeveniment.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Request Body** | JSON |
| **Response** | `201 Created` |

**Request:**
```json
{
  "name": "Concert de Jazz",
  "description": "Concert en directe a Sa Feixina",
  "category": "MUSICA",
  "startDate": "2026-03-01T20:00:00",
  "endDate": "2026-03-01T23:00:00",
  "latitude": 39.57120000,
  "longitude": 2.63420000,
  "address": "Sa Feixina, Palma",
  "localId": 1
}
```

> `localId` és opcional (pot ser `null`).

**Errors:**
| Codi | Descripció |
|---|---|
| `400` | Dades invàlides |
| `401` | No autenticat |
| `404` | Local no trobat |

---

#### `PUT /api/events/{id}`

Editar un esdeveniment existent. Només el creador.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT, només creador) |
| **Request Body** | JSON (mateixos camps que POST) |
| **Response** | `200 OK` |

**Errors:**
| Codi | Descripció |
|---|---|
| `400` | Dades invàlides |
| `401` | No autenticat |
| `403` | No ets el creador |
| `404` | Esdeveniment no trobat |

---

#### `DELETE /api/events/{id}`

Eliminar un esdeveniment. Només el creador.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT, només creador) |
| **Response** | `204 No Content` |

**Errors:**
| Codi | Descripció |
|---|---|
| `401` | No autenticat |
| `403` | No ets el creador |
| `404` | Esdeveniment no trobat |

---

### 3. Likes / Favorits (`/api/events/{id}/like`)

#### `POST /api/events/{id}/like`

Marcar un esdeveniment com a favorit.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Response** | `201 Created` |

**Errors:**
| Codi | Descripció |
|---|---|
| `401` | No autenticat |
| `404` | Esdeveniment no trobat |
| `409` | Ja has marcat aquest esdeveniment |

---

#### `DELETE /api/events/{id}/like`

Eliminar un esdeveniment dels favorits.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Response** | `204 No Content` |

**Errors:**
| Codi | Descripció |
|---|---|
| `401` | No autenticat |
| `404` | Esdeveniment o like no trobat |

---

#### `GET /api/events/{id}/likes/count`

Obtenir el nombre total de likes d'un esdeveniment.

| Camp | Detall |
|---|---|
| **Accés** | Públic |
| **Response** | `200 OK` |

**Response (200):**
```json
{
  "likes": 5
}
```

---

### 4. Usuaris (`/api/users`)

#### `GET /api/users/me`

Obtenir el perfil de l'usuari autenticat.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Response** | `200 OK` |

**Response (200):**
```json
{
  "id": 1,
  "name": "Luka",
  "email": "luka@scena.com"
}
```

---

#### `GET /api/users/me/likes`

Obtenir la llista d'esdeveniments favorits de l'usuari autenticat.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Response** | `200 OK` — Array d'esdeveniments |

---

#### `GET /api/users/usuario/{id}`

Obtenir un usuari per ID.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |

**Errors:**
| Codi | Descripció |
|---|---|
| `404` | Usuari no trobat |

---

#### `GET /api/users/name/{name}`

Obtenir un usuari per nom.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |

**Errors:**
| Codi | Descripció |
|---|---|
| `404` | Usuari no trobat |

---

#### `GET /api/users/email/{email}`

Obtenir un usuari per email.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |

**Errors:**
| Codi | Descripció |
|---|---|
| `404` | Usuari no trobat |

---

### 5. Locals (`/api/locals`) — *Extra*

#### `GET /api/locals`

Llistar tots els locals.

| Camp | Detall |
|---|---|
| **Accés** | Públic |
| **Response** | `200 OK` — Array de locals |

---

#### `GET /api/locals/{id}`

Obtenir un local per ID.

| Camp | Detall |
|---|---|
| **Accés** | Públic |

**Errors:**
| Codi | Descripció |
|---|---|
| `404` | Local no trobat |

---

#### `GET /api/locals/user`

Obtenir els locals de l'usuari autenticat.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Response** | `200 OK` — Array de locals |

---

#### `POST /api/locals`

Crear un nou local.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT) |
| **Request Body** | JSON |
| **Response** | `201 Created` |

**Request:**
```json
{
  "name": "Sa Feixina",
  "latitude": 39.57120000,
  "longitude": 2.63420000,
  "ubication": "Palma, Mallorca",
  "capacity": 500,
  "rooms": 1
}
```

**Errors:**
| Codi | Descripció |
|---|---|
| `400` | Dades invàlides |
| `401` | No autenticat |
| `409` | Local duplicat |

---

#### `PUT /api/locals/{id}`

Editar un local existent. Només el creador.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT, només creador) |
| **Request Body** | JSON (mateixos camps que POST) |
| **Response** | `200 OK` |

**Errors:**
| Codi | Descripció |
|---|---|
| `401` | No autenticat |
| `403` | No ets el creador |
| `404` | Local no trobat |

---

#### `DELETE /api/locals/{id}`

Eliminar un local. Només el creador.

| Camp | Detall |
|---|---|
| **Accés** | 🔒 Protegit (JWT, només creador) |
| **Response** | `204 No Content` |

**Errors:**
| Codi | Descripció |
|---|---|
| `401` | No autenticat |
| `403` | No ets el creador |
| `404` | Local no trobat |

---

## 📊 Resum d'Endpoints

| # | Mètode | Ruta | Accés | Descripció |
|---|---|---|---|---|
| 1 | `POST` | `/api/auth/register` | Públic | Registrar usuari |
| 2 | `POST` | `/api/auth/login` | Públic | Login (retorna JWT) |
| 3 | `GET` | `/api/events` | Públic | Llistar esdeveniments (amb filtres) |
| 4 | `GET` | `/api/events/{id}` | Públic | Detall d'un esdeveniment |
| 5 | `POST` | `/api/events` | 🔒 JWT | Crear esdeveniment |
| 6 | `PUT` | `/api/events/{id}` | 🔒 JWT (creador) | Editar esdeveniment |
| 7 | `DELETE` | `/api/events/{id}` | 🔒 JWT (creador) | Eliminar esdeveniment |
| 8 | `POST` | `/api/events/{id}/like` | 🔒 JWT | Afegir a favorits |
| 9 | `DELETE` | `/api/events/{id}/like` | 🔒 JWT | Eliminar de favorits |
| 10 | `GET` | `/api/events/{id}/likes/count` | Públic | Comptador de likes |
| 11 | `GET` | `/api/users/me` | 🔒 JWT | Perfil propi |
| 12 | `GET` | `/api/users/me/likes` | 🔒 JWT | Esdeveniments favorits |
| 13 | `GET` | `/api/users/usuario/{id}` | 🔒 JWT | Usuari per ID |
| 14 | `GET` | `/api/users/name/{name}` | 🔒 JWT | Usuari per nom |
| 15 | `GET` | `/api/users/email/{email}` | 🔒 JWT | Usuari per email |
| 16 | `GET` | `/api/locals` | Públic | Llistar locals |
| 17 | `GET` | `/api/locals/{id}` | Públic | Detall d'un local |
| 18 | `GET` | `/api/locals/user` | 🔒 JWT | Locals de l'usuari |
| 19 | `POST` | `/api/locals` | 🔒 JWT | Crear local |
| 20 | `PUT` | `/api/locals/{id}` | 🔒 JWT (creador) | Editar local |
| 21 | `DELETE` | `/api/locals/{id}` | 🔒 JWT (creador) | Eliminar local |

**Total: 21 endpoints** (5 públics, 16 protegits amb JWT)
