package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Order(var id: Long, customerID: String, produktID: String, ammount: String, extras: String, price: String, orderTime: String)

