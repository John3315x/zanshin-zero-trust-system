CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('USER', 'ADMIN', 'SUPPORT', 'AUDITOR')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,

    user_id INTEGER NOT NULL,

    token_id VARCHAR(36) UNIQUE NOT NULL,

    refresh_token_hash TEXT UNIQUE NOT NULL,

    ip VARCHAR(45),
    user_agent TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,

    revoked BOOLEAN DEFAULT FALSE,

    /*
    * Se elimina bajo arquitectura de microservicios, cada servicio maneja su propia base de datos y no hay necesidad de relaciones directas entre tablas.
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
    */

);

CREATE TABLE security_logs (
    id SERIAL PRIMARY KEY,

    user_id INTEGER NULL,

    event_type VARCHAR(50) NOT NULL,
    ip VARCHAR(45),
    user_agent TEXT,

    details TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    /*
    * Se elimina bajo arquitectura de microservicios, cada servicio maneja su propia base de datos y no hay necesidad de relaciones directas entre tablas.
    CONSTRAINT fk_user_log
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
     */
);