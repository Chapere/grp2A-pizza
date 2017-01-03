# --- !Ups
 
CREATE TABLE Users (
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    adress VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    plz VARCHAR(255) NOT NULL,
    distance NUMERIC NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR (255) NOT NULL,
    activeFlag INT NOT NULL
);

INSERT INTO Users (name, lastname, adress, city, plz, distance, email, password, activeFlag)
VALUES ('Padron', 'Schulz', 'Lerchenauer Str. 12', 'München', 80935, 20, 'user', 'user', 1);

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

INSERT INTO Employees (name, lastname, workplace, acces,accesLevel , netRate, email, password, activeFlag)
VALUES ('Emil', 'Hubert', 'IT', 'root', 10, 15.84, 'root', 'root', 1);

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC NOT NULL,
    ingredients VARCHAR(255) NOT NULL,
    comment VARCHAR(255) NOT NULL,
    supplements VARCHAR(255) NOT NULL
);

INSERT INTO Pizzas (id, name, price, ingredients, comment, supplements)
VALUES (1, 'Margherita', 0.60, 'Pizzateig, Tomaten, Käse', 'Der Klassiker', '1,2,5'),
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
VALUES (1, 'Coca-Cola', 1.45, 0.5, 'Liter'),
      (2, 'Fanta', 1.20, 0.30, 'Liter'),
      (3, 'Sprite', 2.00, 1, 'Liter'),
      (4, 'Pommes Frites', 1.99, 500, 'Gramm'),
      (5, 'Chicken-Wings', 2.99, 14, 'Stück');

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
    totalPrice NUMERIC NOT NULL,
    orderTime VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL
);