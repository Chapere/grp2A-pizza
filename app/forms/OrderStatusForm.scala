package forms

/**
  * Form containing data to set/change the status of an order.
  *
  * @param orderID       ID of the order
  * @param orderStatusKZ Status of the order e.g. "ausgeliefert"
  */
case class OrderStatusForm(orderID: Long, orderStatusKZ: String)
