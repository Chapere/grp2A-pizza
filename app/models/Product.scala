package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Product(var id: Long, var name: String,
                   var price: Double, var size: Double,
                   var unit: String)

