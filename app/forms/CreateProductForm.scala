package forms

/**
  * Form containing data to create a product.
  * @param id ID of the product
  * @param name name ID of the product
  * @param price price ID of the product
  * @param size size ID of the product
  * @param unit unit ID of the product
  */
case class CreateProductForm(id: Long, name: String, price: Double, size: Double, unit: String)
