# --- !Ups
 
CREATE TABLE Users (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    adress VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    plz VARCHAR(255) NOT NULL,
    distance VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR (255) NOT NULL,
    activeFlag INT NOT NULL
);

INSERT INTO Users (id, name, lastname, adress, city, plz, distance, email, password, activeFlag)
VALUES (1, 'Padron', 'Schulz', 'Lerchenauer Str. 12', 'München', 80935, '19 km', 'user', 'user', 1);

CREATE TABLE Employees (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    workplace VARCHAR(255) NOT NULL,
    acces VARCHAR(255) NOT NULL,
    accesLevel INT NOT NULL,
    netRate NUMERIC NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    activeFlag INT NOT NULL
);

INSERT INTO Employees (id, name, lastname, workplace, acces,accesLevel , netRate, email, password, activeFlag)
VALUES (1, 'Emil', 'Hubert', 'IT', 'root', 10, 15.84, 'root', 'root', 1);

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    supplements VARCHAR(255) NOT NULL
);

INSERT INTO Pizzas (id, name, price, ingredients, comment, supplements)
VALUES (0, '', 0, '', '', ''),
       (1, 'Margherita', 0.60, 'Pizzateig, Tomaten, Käse', 'Der Klassiker', '1,2,5'),
       (2, 'Funghi', 0.70, 'Pizzateig, Tomaten, Champignions, Schinken, Käse', 'Für Entwickler', '1,2,3,5,8'),
       (3, 'Hawaii', 0.90, 'Pizzateig, Tomaten, Ananas, Käse', 'Des Deutschen liebste', '1,2,3,5,6,7,8');


CREATE TABLE Products (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    size NUMERIC NOT NULL,
    unit VARCHAR(255) NOT NULL
);

INSERT INTO Products (id, name, price, size, unit)
VALUES (0, '', 0, 0, ''),
       (1, 'Coca-Cola', 1.45, 0.5, 'Liter'),
       (2, 'Fanta', 1.20, 0.30, 'Liter'),
       (3, 'Sprite', 2.00, 1, 'Liter'),
       (4, 'Pommes Frites', 1.99, 500, 'Gramm'),
       (5, 'Chicken-Wings', 2.99, 14, 'Stück');

CREATE TABLE Extras (
    id serial PRIMARY KEY,
    name VARCHAR(255),
    price NUMERIC NOT NULL
);

INSERT INTO Extras (id, name, price)
VALUES (0, '', 0),
       (1, 'Champignions', 0.80),
       (2, 'Paprika', 0.60),
       (3, 'Käse', 0.50),
       (4, 'Schinken', 0.90);

CREATE TABLE Orders (
    id serial PRIMARY KEY,
    customerID NUMERIC NOT NULL,
    pizzaID NUMERIC NOT NULL,
    productID NUMERIC NOT NULL,
    pizzaName VARCHAR(255) NOT NULL,
    productName VARCHAR(255) NOT NULL,
    pizzaAmount NUMERIC NOT NULL,
    pizzaSize NUMERIC NOT NULL,
    pizzaPrice NUMERIC NOT NULL,
    productAmount NUMERIC NOT NULL,
    productPrice NUMERIC NOT NULL,
    extraOneID NUMERIC NOT NULL,
    extraOneName VARCHAR(255) NOT NULL,
    extraOnePrice NUMERIC NOT NULL,
    extraTwoID NUMERIC NOT NULL,
    extraTwoName VARCHAR(255) NOT NULL,
    extraTwoPrice NUMERIC NOT NULL,
    extraThreeID NUMERIC NOT NULL,
    extraThreeName VARCHAR(255) NOT NULL,
    extraThreePrice NUMERIC NOT NULL,
    extrasString VARCHAR(255) NOT NULL,
    extrasTotalPrice NUMERIC NOT NULL,
    totalPrice NUMERIC NOT NULL,
    orderTime VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    deliveryTime VARCHAR(255) NOT NULL
);