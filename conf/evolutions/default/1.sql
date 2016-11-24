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

CREATE TABLE Pizzas (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price VARCHAR(255) NOT NULL
);
 
# --- !Downs
 
DROP TABLE User;
