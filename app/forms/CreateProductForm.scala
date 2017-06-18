package forms

/**
  * Form containing data to create a product.
  *
  * @param id    ID of the product
  * @param name  name of the product
  * @param price price of the product
  * @param size  size of the product
  * @param unit  unit of the product
  */
case class CreateProductForm(id: Long, name: String, price: Double, size: Double, unit: String)
