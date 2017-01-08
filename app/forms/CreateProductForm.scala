package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreateProductForm(id: Long, name: String, price: Double, size: Double, unit: String)
