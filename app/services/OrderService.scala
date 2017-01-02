package services

import dbaccess.{OrderDao, OrderDaoT}
import models.Order

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

  def addOrder(customerID: Long, produktID: Long, amount: Int, extras: String, price: Double, orderTime: String, size: Double): Order = {
    // create User
    val newOrder = Order(-1, customerID, produktID, amount, extras, price, orderTime, size)
    // persist and return User
    orderDao.addOrder(newOrder)
  }

  def createOrder(customerID: Long, produktID: Long, amount: Int, extras: String, price: Double, orderTime: String, size: Double): Double = {
    // create User
    val newOrder = Order(-1, customerID, produktID, amount, extras, price, orderTime, size)
    // persist and return User
    orderDao.createOrder(newOrder)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmOrder(id: Long): Boolean = OrderDao.rmOrder(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def availableOrder: List[Order] = {
    OrderDao.availableOrders
  }

}

object OrderService extends OrderServiceT
