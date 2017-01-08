package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreateOrderForm(userID: Double, pizzaID: Double, productID: Double, pizzaAmount: Int, pizzaSize: Double, productAmount: Int,
                           extraOneID: Double, extraTwoID: Double, extraThreeID: Double)
