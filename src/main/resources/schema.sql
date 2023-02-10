CREATE TABLE IF NOT EXISTS speaker
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(100),
    company VARCHAR(100),
    talk    VARCHAR(100)
);

INSERT INTO speaker(name, company, talk)
VALUES ('Manuel Ernesto', 'Standard Bank Angola', 'Async')