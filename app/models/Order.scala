package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Order(var id: Long, customerID: Double, pizzaID: Double, productID: Double, pizzaName: String,
                 productName: String, pizzaAmount: Double, pizzaSize: Double, pizzaPrice: Double, productAmount: Double,
                 productPrice: Double, totalPrice: Double, orderTime: String, status: String)

