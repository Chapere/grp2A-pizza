package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreatePizzaForm(id: Long, name: String, price: Double, ingredients: String, comment: String, supplements: String)
