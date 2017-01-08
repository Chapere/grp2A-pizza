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
    // create Product
    val newProduct = Product(-1, name, price, size, unit)
    // persist and return Product
    productDao.addProduct(newProduct)
  }

  def selectProduct(id: Long): Product = {
    // create Product
    // persist and return Product
    ProductDao.selectProductByIdentification(id)
  }


  def updateProduct(id: Long, name: String, price: Double, size: Double, unit: String): Product = {
    // create Product
    val updateProductService = Product(id, name, price, size, unit)
    // persist and return Product
    ProductDao.updateProductDao(updateProductService)
  }


  def getProductByID(id: Long): List[Product] = {
    // create Product
    // persist and return Product
    productDao.getProductByIdentification(id)
  }

  def selectProductByID(id: Long): Product = {
    // create Product
    // persist and return Product
    productDao.selectProductByIdentification(id)
  }

  /**
    * Removes a product by id from the system.
    * @param id products id.
    * @return a boolean success flag.
    */
  def rmProduct(id: Long): Boolean = ProductDao.rmProduct(id)

  /**
    * Gets a list of all registered products.
    * @return list of products.
    */
  def availableProducts: List[Product] = {
    productDao.registeredProducts
  }

}

object ProductService extends ProductServiceT
