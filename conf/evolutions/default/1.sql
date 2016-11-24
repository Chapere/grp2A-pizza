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

INSERT INTO Pizzas (name, price, ingredients, comment, supplements)
VALUES (('Margherita', 'Funghi', 'Zwiebel', 'Hawaii'), (7.50, 8.20, 8.90, 9.99),
        ('Pizzateig, Tomaten, Käse, Gewürze', 'Pizzateig, Tomaten, Schinken, Champignions, Käse, Gewürze',
         'Pizzateig, Tomaten, Schinken, Zwiebeln, Käse, Gewürze', 'Pizzateig, Tomaten, Käse, Annanas, Gewürze'),
        ('Die Originale', 'Der Klassisker mit Champignions', 'Für den guten Atem', 'Der deutschen Liebste'),
        ('', '1,2,3', '1,6,4', '1,2,3,4,5,6,7,8,9'));
INSERT INTO Employees (name, lastname, workplace, acces) VALUES ('Padron', 'Schulz', 'IT', 'root');

# --- !Downs
 
DROP TABLE Users;
DROP TABLE Pizzas;