# Users schema
 
# --- !Ups
 
CREATE TABLE Users (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    adress VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    plz VARCHAR(255) NOT NULL
);

CREATE TABLE Employees (
    id serial PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    workplace VARCHAR(255) NOT NULL,
    acces VARCHAR(255) NOT NULL
);

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    supplements VARCHAR(255) NOT NULL
);

INSERT INTO Pizzas (name, price, ingredients, comment, supplements) VALUES ('Margherita', 2.40, 'Pizza', 'Pizza', '1');

INSERT INTO Users (name, lastname, adress, city, plz) VALUES ('Padron', 'Schulz', 'Lerchenauer Str. 12', 'München', 80935);

# --- !Downs
