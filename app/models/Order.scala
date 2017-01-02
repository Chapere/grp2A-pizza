package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Order(var id: Long, customerID: Long, produktID: Long, amount: Int, extras: String, price: Double, orderTime: String, size: Double)

