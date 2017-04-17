package forms

/**
  * Form containing data to create a pizza.
  * @param id ID of the pizza
  * @param name name of the pizza
  * @param price price of the pizza
  * @param ingredients ingredients of the pizza
  * @param comment comment on the pizza
  * @param supplements supplements of the pizza
  */
case class CreatePizzaForm(id: Long, name: String, price: Double, ingredients: String,
                           comment: String, supplements: String)
