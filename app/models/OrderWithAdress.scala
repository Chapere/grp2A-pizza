package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class OrderWithAdress(var id: Long, customerID: Double, pizzaID: Double,
                           productID: Double, pizzaName: String,
                           productName: String, pizzaAmount: Double,
                           pizzaSize: Double, pizzaPrice: Double,
                           productAmount: Double, productPrice: Double,
                           totalPrice: Double, orderTime: String,
                           status: String, var id2: Long,
                           var name: String, var lastname: String,
                           var adress: String, var city: String,
                           var plz: String, distance: Double,
                           email: String, password: String,
                           activeFlag: Int, extrasString: String,
                           extraTotalPrice: Double, deliveryTime: String)

