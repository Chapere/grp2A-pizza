package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class OrdersWithExtras(var id: Long, customerID: Double, pizzaID: Double,
                            productID: Double, pizzaName: String,
                            productName: String, pizzaAmount: Double,
                            pizzaSize: Double, pizzaPrice: Double,
                            productAmount: Double, productPrice: Double,
                            extraOneID: Double, extraOneName: String,
                            extraOnePrice: Double, extraTwoID: Double,
                            extraTwoName: String, extraTwoPrice: Double,
                            extraThreeID: Double, extraThreeName: String,
                            extraThreePrice: Double, extrasString: String,
                            extrasTotalPrice: Double, totalPrice: Double,
                            orderTime: String, status: String,
                            deliveryTime: String)

