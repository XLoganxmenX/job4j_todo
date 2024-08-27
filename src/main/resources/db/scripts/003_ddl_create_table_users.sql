CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    name     varchar        not null,
    login    varchar unique not null,
    password varchar        not null
);