package services

import dbaccess._
import models.Product

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait ProductServiceT {

  val productDao: ProductDaoT = ProductDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addProduct(name: String, price: Double, size: Double, unit: String): Product = {
    // create User
    val newProduct = Product(-1, name, price, size, unit)
    // persist and return User
    ProductDao.addProduct(newProduct)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmProduct(id: Long): Boolean = ProductDao.rmProduct(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def availableProduct: List[Product] = {
    ProductDao.availableProducts
  }

}

object ProductService extends ProductServiceT
