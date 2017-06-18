package models

/**
  * Product entity.
  *
  * @param id    the id of a product
  * @param name  the name of a product
  * @param price the price of a product
  * @param size  the size of a product
  * @param unit  the unit a product comes with
  */
case class Product(var id: Long, var name: String,
                   var price: Double, var size: Double,
                   var unit: String)

