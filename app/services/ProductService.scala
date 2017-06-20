package services

import dbaccess.{ProductDao, ProductDaoT}
import models.Product

/**
  * Service class for product related operations.
  *
  * @author ob, scs
  */
trait ProductServiceT {

  val productDao: ProductDaoT = ProductDao

  /**
    * Adds a new product to the system.
    *
    * @param name  the name of a product
    * @param price the price of a product
    * @param size  the size of a product
    * @param unit  the unit a product comes with
    * @return the new product
    */
  def addProduct(name: String, price: Double, size: Double, unit: String): Product = {
    // create Product
    val newProduct = Product(-1, name, price, size, unit)
    // persist and return Product
    productDao.addProduct(newProduct)
  }

  /**
    * Retrieves a product from the database.
    *
    * @param id the product's id
    * @return the product object
    */
  def selectProduct(id: Long): Product = {
    // create Product
    // persist and return Product
    ProductDao.selectProductByIdentification(id)
  }

  /**
    * Changes the database entry of the given product.
    *
    * @param id    the id of a product
    * @param name  the name of a product
    * @param price the price of a product
    * @param size  the size of a product
    * @param unit  the unit a product comes with
    * @return the changed product object
    */
  def updateProduct(id: Long, name: String, price: Double, size: Double, unit: String): Product = {
    // create Product
    val updateProductService = Product(id, name, price, size, unit)
    // persist and return Product
    ProductDao.updateProductDao(updateProductService)
  }

  /**
    * Retrieves a product from the database.
    *
    * @param id the product's id
    * @return the product object
    */
  def getProductByID(id: Long): List[Product] = {
    // create Product
    // persist and return Product
    productDao.getProductByIdentification(id)
  }

  /**
    * Retrieves a product from the database.
    *
    * @param id the product's id
    * @return the product object
    */
  def selectProductByID(id: Long): Product = {
    // create Product
    // persist and return Product
    productDao.selectProductByIdentification(id)
  }

  /**
    * Removes a product from the database.
    *
    * @param id the product's id
    * @return a boolean success flag
    */
  def rmProduct(id: Long): Boolean = ProductDao.rmProduct(id)

  /**
    * Retrieves a list of available products from the database.
    *
    * @return a list of product objects
    */
  def availableProducts: List[Product] = {
    productDao.registeredProducts
  }
}

object ProductService extends ProductServiceT
