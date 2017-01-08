package services

import dbaccess.{OrderDao, OrderDaoT}
import models.{Extra, Order, OrderWithAdress, OrdersWithExtras}

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait OrderServiceT {

  val orderDao: OrderDaoT = OrderDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */


  def createOrder(customerID: Double, pizzaID: Double, productID: Double,
                  pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                  pizzaPrice: Double, productAmount: Double, productPrice: Double,
                  extraOneID: Double, extraOneName: String, extraOnePrice: Double,
                  extraTwoID: Double, extraTwoName: String, extraTwoPrice: Double,
                  extraThreeID: Double, extraThreeName: String, extraThreePrice: Double,
                  orderTime: String, status: String, deliveryTime: String): OrdersWithExtras = {
    // create User
    val newOrder = Order(-1, customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, extraOneID, "N/A", 0, extraTwoID, "N/A", 0, extraThreeID, "N/A", 0, 0, orderTime, "Bestellung empfangen", deliveryTime)
    // persist and return User
    orderDao.createOrder(newOrder)
  }

  def getOrderbyID(orderID: Double): Order = {
    // create User
    // persist and return User
    orderDao.getOrder(orderID)
  }


  def orderSetStaus(id: Long, orderStatus: String): Order = {
    // create User
    // persist and return User
    orderDao.setOrderStatus(id, orderStatus)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmOrder(id: Double): Boolean = OrderDao.rmOrder(id)

  def deactivateOrder(id: Double): Boolean = OrderDao.deactivateOrder(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def availableOrder: List[Order] = {
    OrderDao.availableOrders
  }

  def availableOrderByID(id: Double): List[Order] = {
    OrderDao.availableOrdersByID(id)
  }

  def availableOrderWithAdress: List[OrderWithAdress] = {
    OrderDao.availableOrdersWithAdress
  }


}

object OrderService extends OrderServiceT
