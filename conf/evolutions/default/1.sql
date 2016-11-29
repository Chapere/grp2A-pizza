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
    acces VARCHAR(255) NOT NULL,
    netRate VARCHAR(255) NOT NULL
);

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    supplements VARCHAR(255) NOT NULL
);

CREATE TABLE Orders (
    id serial PRIMARY KEY,
    customerID VARCHAR(255) NOT NULL,
    produktID VARCHAR(255) NOT NULL,
    ammount VARCHAR(255) NOT NULL,
    extras VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL,
    orderTime VARCHAR(255) NOT NULL
);


INSERT INTO Pizzas (name, price, ingredients, comment, supplements)
VALUES ('Margherita', 5.90, 'Pizzateig, Tomaten, Käse', 'Der Klassiker', '1,2,5'),
    ('Funghi', 6.99, 'Pizzateig, Tomaten, Champignions, Schinken, Käse', 'Für Entwickler', '1,2,3,5,8'),
    ('Hawaii', 8.76, 'Pizzateig, Tomaten, Ananas, Käse', 'Des Deutschen liebste', '1,2,3,5,6,7,8');

INSERT INTO Users (name, lastname, adress, city, plz) VALUES ('Padron', 'Schulz', 'Lerchenauer Str. 12', 'München', 80935);
INSERT INTO Employees (id, name, lastname, workplace, acces, netRate) ('Emil', 'Hubert', 'IT', 'root', '15.84')