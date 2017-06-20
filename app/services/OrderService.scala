package services

import dbaccess.{OrderDao, OrderDaoT}
import models.{Extra, Order, OrderWithAdress, OrdersWithExtras}

/**
  * Service class for order related operations.
  *
  * @author ob, scs
  */
trait OrderServiceT {
  val na = "N/A"
  val orderDao: OrderDaoT = OrderDao

  /**
    * Adds a new order to the system.
    *
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
    * @param extraOneID      the id of the first extra
    * @param extraOneName    the name of the first extra
    * @param extraOnePrice   the price of the first extra
    * @param extraTwoID      the id of the second extra
    * @param extraTwoName    the name of the second extra
    * @param extraTwoPrice   the price of the second extra
    * @param extraThreeID    the id of the third extra
    * @param extraThreeName  the name of the third extra
    * @param extraThreePrice the price of the third extra
    * @param orderTime       the time an order has been placed
    * @param status          the status of an order
    * @param deliveryTime    the time an order will be delivered
    * @return the new order
    */
  def createOrder(customerID: Double, pizzaID: Double, productID: Double,
                  pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                  pizzaPrice: Double, productAmount: Double, productPrice: Double,
                  extraOneID: Double, extraOneName: String, extraOnePrice: Double,
                  extraTwoID: Double, extraTwoName: String, extraTwoPrice: Double,
                  extraThreeID: Double, extraThreeName: String, extraThreePrice: Double,
                  orderTime: String, status: String, deliveryTime: String): OrdersWithExtras = {
    // create User
    val newOrder = Order(-1, customerID, pizzaID, productID, pizzaName,
      productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, extraOneID, na, 0,
      extraTwoID, na, 0, extraThreeID, na, 0, 0,
      orderTime, "Bestellung empfangen", deliveryTime)
    // persist and return User
    orderDao.createOrder(newOrder)
  }

  /**
    * Retrieves an order from the database.
    *
    * @param orderID the order's id
    * @return the order object
    */
  def getOrderbyID(orderID: Double): Order = {
    // create User
    // persist and return User
    orderDao.getOrder(orderID)
  }

  /**
    * Updates the status of an order in the database.
    *
    * @param id          the order's id
    * @param orderStatus the new status of the order
    * @return the order object with changed status
    */
  def orderSetStaus(id: Long, orderStatus: String): Order = {
    // create User
    // persist and return User
    orderDao.setOrderStatus(id, orderStatus)
  }

  /**
    * Removes an order from the database.
    *
    * @param id the order's id
    * @return a boolean success flag
    */
  def rmOrder(id: Double): Boolean = OrderDao.rmOrder(id)

  /**
    * Deactivates an order in the database.
    *
    * @param id the order's id
    * @return a boolean success flag
    */
  def deactivateOrder(id: Double): Boolean = OrderDao.deactivateOrder(id)

  /**
    * Retrieves a list of available orders from the database.
    *
    * @return a list of order objects
    */
  def availableOrder: List[Order] = {
    OrderDao.availableOrders
  }

  /**
    * Retrieves an order from the database.
    *
    * @param id the order's id
    * @return the order object
    */
  def availableOrderByID(id: Double): List[Order] = {
    OrderDao.availableOrdersByID(id)
  }

  /**
    * Retrieves a list of available orders from the database including the customers's address.
    *
    * @return a list of order objects
    */
  def availableOrderWithAdress: List[OrderWithAdress] = {
    OrderDao.availableOrdersWithAdress
  }
}

object OrderService extends OrderServiceT
