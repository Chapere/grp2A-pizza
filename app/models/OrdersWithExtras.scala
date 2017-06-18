package models

/**
  * Entity for order with extras.
  *
  * @param id               the id of an order
  * @param customerID       the id of a customer
  * @param pizzaID          the id of a pizza
  * @param productID        the id of a product
  * @param pizzaName        the name of a pizza
  * @param productName      the name of a product
  * @param pizzaAmount      the ordered quantity of a pizza
  * @param pizzaSize        the size of a pizza
  * @param pizzaPrice       the price of a pizza
  * @param productAmount    the ordered quantity of a product
  * @param productPrice     the price of a product
  * @param extraOneID       the id of the first extra
  * @param extraOneName     the name of the first extra
  * @param extraOnePrice    the price of the first extra
  * @param extraTwoID       the id of the second extra
  * @param extraTwoName     the name of the second extra
  * @param extraTwoPrice    the price of the second extra
  * @param extraThreeID     the id of the third extra
  * @param extraThreeName   the name of the third extra
  * @param extraThreePrice  the price of the third extra
  * @param extrasString     string containing the names of all extras
  * @param extrasTotalPrice the total price of all extras
  * @param totalPrice       the total price of an order
  * @param orderTime        the time an order has been placed
  * @param status           the status of an order
  * @param deliveryTime     the time an order will be delivered
  */
case class OrdersWithExtras(var id: Long, customerID: Double, pizzaID: Double,
                            productID: Double, pizzaName: String,
                            productName: String, pizzaAmount: Double,
                            pizzaSize: Double, pizzaPrice: Double,
                            productAmount: Double, productPrice: Double,
                            extraOneID: Double, extraOneName: String,
                            extraOnePrice: Double, extraTwoID: Double,
                            extraTwoName: String, extraTwoPrice: Double,
                            extraThreeID: Double, extraThreeName: String,
                            extraThreePrice: Double, extrasString: String,
                            extrasTotalPrice: Double, totalPrice: Double,
                            orderTime: String, status: String,
                            deliveryTime: String)

