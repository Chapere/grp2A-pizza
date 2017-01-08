package forms

/**
 * Form containing data to create a pizza.
 * @param name name of the pizza.
 */
case class CreatePizzaForm(name: String, price: Double, ingredients: String, comment: String, supplements: String)
