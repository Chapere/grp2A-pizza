package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Order(var id: Long, customerID: Int, produktID: Int, amount: Int, extras: String, price: Double, orderTime: String, size: Double)

