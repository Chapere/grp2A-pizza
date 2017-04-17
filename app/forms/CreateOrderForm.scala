package forms

/**
  * Form containing data to create a user.
  * @param userID ID of the user
  * @param pizzaID ID of the pizza
  * @param productID ID of the product
  * @param pizzaAmount amount of pizzas
  * @param pizzaSize size of the pizza
  * @param productAmount amount of products that will be ordererd
  * @param extraOneID ID of the extra
  * @param extraTwoID ID of the second extra
  * @param extraThreeID ID of the third extra
  */
case class CreateOrderForm(userID: Double, pizzaID: Double, productID: Double, pizzaAmount: Int,
                           pizzaSize: Double, productAmount: Int, extraOneID: Double,
                           extraTwoID: Double, extraThreeID: Double)
