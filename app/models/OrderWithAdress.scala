package models

/**
  * Entity for order containing the address of the customer.
  *
  * @param id              the id of an order
  * @param customerID      the id of a customer
  * @param pizzaID         the id of a pizza
  * @param productID       the id of a product
  * @param pizzaName       the name of a pizza
  * @param productName     the name of a product
  * @param pizzaAmount     the ordered quantity of a pizza
  * @param pizzaSize       the size of a pizza
  * @param pizzaPrice      the price of a pizza
  * @param productAmount   the ordered quantity of a product
  * @param productPrice    the price of a product
  * @param totalPrice      the total price of an order
  * @param orderTime       the time an order has been placed
  * @param status          the status of an order
  * @param id2             the id of the customer
  * @param name            the name of the customer
  * @param lastname        the lastname of the customer
  * @param adress          the adress of the customer
  * @param city            the city of the customer
  * @param plz             the plz of the customer
  * @param distance        the distance to the address of the customer
  * @param email           the email of the customer
  * @param password        the password of the customer
  * @param activeFlag      the activeFlag
  * @param extrasString    string containing the names of all extras
  * @param extraTotalPrice the total price of all extras
  * @param deliveryTime    the time an order will be delivered
  */
case class OrderWithAdress(var id: Long, customerID: Double, pizzaID: Double,
                           productID: Double, pizzaName: String,
                           productName: String, pizzaAmount: Double,
                           pizzaSize: Double, pizzaPrice: Double,
                           productAmount: Double, productPrice: Double,
                           totalPrice: Double, orderTime: String,
                           status: String, var id2: Long,
                           var name: String, var lastname: String,
                           var adress: String, var city: String,
                           var plz: String, distance: Double,
                           email: String, password: String,
                           activeFlag: Int, extrasString: String,
                           extraTotalPrice: Double, deliveryTime: String)

