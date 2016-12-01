# --- !Ups
 
CREATE TABLE Users (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    adress VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    plz VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR (255) NOT NULL
);

CREATE TABLE Employees (
    id serial PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    workplace VARCHAR(255) NOT NULL,
    acces VARCHAR(255) NOT NULL,
    netRate VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    supplements VARCHAR(255) NOT NULL
);

CREATE TABLE Orders (
    id serial PRIMARY KEY,
    customerID NUMERIC NOT NULL,
    produktID NUMERIC NOT NULL,
    amount VARCHAR(255) NOT NULL,
    extras VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    orderTime VARCHAR(255) NOT NULL,
    size NUMERIC NOT NULL
);

INSERT INTO Pizzas (id, name, price, ingredients, comment, supplements)
VALUES (1, 'Margherita', 0.60, 'Pizzateig, Tomaten, Käse', 'Der Klassiker', '1,2,5'),
    (2, 'Funghi', 0.70, 'Pizzateig, Tomaten, Champignions, Schinken, Käse', 'Für Entwickler', '1,2,3,5,8'),
    (3, 'Hawaii', 0.90, 'Pizzateig, Tomaten, Ananas, Käse', 'Des Deutschen liebste', '1,2,3,5,6,7,8');

INSERT INTO Users ( name, lastname, adress, city, plz, email, password) VALUES ('Padron', 'Schulz', 'Lerchenauer Str. 12', 'München', 80935, 'root@pizza-power.de', 'alpine');
INSERT INTO Employees (name, lastname, workplace, acces, netRate, email, password) VALUES ('Emil', 'Hubert', 'IT', 'root', '15.84', 'root', 'root');