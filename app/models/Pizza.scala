package models

/**
  * Pizza Class
  * @author Kamil Gorszczyk
  */

case class Pizza(var id: Long, var name: String,
                 var price: Double, var ingredients: String,
                 var comment: String, var supplements: String)

