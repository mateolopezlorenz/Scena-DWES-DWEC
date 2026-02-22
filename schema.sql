-- ============================================
-- SCENA MVP — Esquema de Base de Dades
-- PostgreSQL
-- ============================================

-- Eliminar taules si existeixen (ordre invers per FK)
DROP TABLE IF EXISTS user_likes CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS local CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- TAULA: users
-- ============================================
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    enabled     BOOLEAN NOT NULL DEFAULT true,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TAULA: local (entitat extra)
-- ============================================
CREATE TABLE local (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    latitude    NUMERIC(10, 8) NOT NULL,
    longitude   NUMERIC(11, 8) NOT NULL,
    ubication   VARCHAR(255) NOT NULL,
    capacity    INTEGER NOT NULL,
    rooms       INTEGER NOT NULL,
    user_id     BIGINT NOT NULL REFERENCES users(id)
);

-- ============================================
-- TAULA: events
-- ============================================
CREATE TABLE events (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    category    VARCHAR(50),           -- MUSICA, ESPORT, CULTURA, ALTRES
    start_date  TIMESTAMP NOT NULL,
    end_date    TIMESTAMP NOT NULL,
    latitude    NUMERIC(10, 8) NOT NULL,
    longitude   NUMERIC(11, 8) NOT NULL,
    address     VARCHAR(255),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    local_id    BIGINT REFERENCES local(id) ON DELETE SET NULL
);

-- ============================================
-- TAULA: user_likes (relació N:M users ↔ events)
-- ============================================
CREATE TABLE user_likes (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    event_id    BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, event_id)
);

-- ============================================
-- DADES INICIALS (SEED)
-- ============================================

-- Usuari de prova (password: 123456 encriptat amb BCrypt)
INSERT INTO users (name, email, password) VALUES
    ('Luka', 'luka@scena.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('Marti', 'marti@scena.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
    ('Mateo', 'mateo@scena.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy');

-- Locals d'exemple
INSERT INTO local (name, latitude, longitude, ubication, capacity, rooms, user_id) VALUES
    ('Sa Feixina', 39.57120000, 2.63420000, 'Palma, Mallorca', 500, 1, 1),
    ('Parc de la Mar', 39.56780000, 2.64900000, 'Palma, Mallorca', 2000, 1, 1),
    ('Poliesportiu Son Moix', 39.58340000, 2.61560000, 'Palma, Mallorca', 1000, 3, 2);

-- Esdeveniments d'exemple
INSERT INTO events (name, description, category, start_date, end_date, latitude, longitude, address, user_id, local_id) VALUES
    ('Concert de Jazz', 'Concert de jazz en directe a Sa Feixina', 'MUSICA', '2026-03-01 20:00:00', '2026-03-01 23:00:00', 39.57120000, 2.63420000, 'Sa Feixina, Palma', 1, 1),
    ('Torneig de Futbol', 'Torneig amateur de futbol 7', 'ESPORT', '2026-03-15 10:00:00', '2026-03-15 18:00:00', 39.58340000, 2.61560000, 'Poliesportiu Son Moix, Palma', 2, 3),
    ('Exposició Fotogràfica', 'Mostra de fotografia de paisatges de Mallorca', 'CULTURA', '2026-04-01 11:00:00', '2026-04-15 20:00:00', 39.56780000, 2.64900000, 'Parc de la Mar, Palma', 1, 2),
    ('Festa Major', 'Festa popular amb música i gastronomia', 'ALTRES', '2026-05-10 18:00:00', '2026-05-10 02:00:00', 39.57500000, 2.65400000, 'Plaça Major, Palma', 3, NULL);

-- Likes d'exemple
INSERT INTO user_likes (user_id, event_id) VALUES
    (1, 2),
    (1, 3),
    (2, 1),
    (3, 1),
    (3, 3);
