package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Product

/**
 * Data access object for product related operations.
 *
 * @author ob, scs
 */
trait ProductDaoT {

  val ID = "id"
  val name = "name"
  val price = "price"
  val size = "size"
  val unit = "unit"
  val select = "SELECT * FROM Products WHERE id = {id};"

  /**
    * Creates the given product in the database.
    *
    * @param product the product object to be stored
    * @return the persisted product object
    */
  def addProduct(product: Product): Product = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Products(name, price, size, unit)" +
          "values ({name}, {price}, {size}, {unit})").on(
          'name -> product.name, 'price -> product.price,
          'size -> product.size, 'unit -> product.unit).executeInsert()
      product.id = id.get
    }
    product
  }

  /**
    * Changes the database entry of the given product.
    *
    * @param product the product object with the change data
    * @return the changed product object
    */
  def updateProductDao(product: Product): Product = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("UPDATE Products SET name = {name}, price = {price}," +
          "size = {size}, unit = {unit} WHERE id = {id}").on(
          'name -> product.name, 'price -> product.price,
          'size -> product.size, 'unit -> product.unit,
          'id -> product.id).executeInsert()
    }
    product
  }

  /**
    * Retrieve a product from the database.
    *
    * @param id the product's id
    * @return the product object
    */
  def getProductByIdentification(id: Long): List[Product] = {
    DB.withConnection { implicit c =>
      val selectProducts = SQL(select).on(
        'id -> id)
      val products = selectProducts().map(row => Product(row[Long](ID),
        row[String](name), row[Double](price),
        row[Double](size), row[String](unit))).toList
      products
    }

  }

  /**
    * Retrieve a product from the database.
    *
    * @param id the product's id
    * @return the product object
    */
  def selectProductByIdentification(id: Long): Product = {
    DB.withConnection { implicit c =>
      val selectProduct = SQL(select).on(
        'id -> id)
      val products = selectProduct().map(row => Product(row[Long](ID),
        row[String](name), row[Double](price),
        row[Double](size), row[String](unit))).toList

      products.head
    }

  }


  /**
    * Removes a product from the database.
    *
    * @param id the product's id
    * @return a boolean success flag
    */
  def rmProduct(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Products where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Retrieves a list of available products from the database.
    *
    * @return a list of product objects
    */
  def registeredProducts: List[Product] = {
    DB.withConnection { implicit c =>
      val selectProducts = SQL("Select id, name, price, size, unit from Products;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val products = selectProducts().map(row => Product(row[Long](ID),
        row[String](name), row[Double](price),
        row[Double](size), row[String](unit))).toList
      products
    }
  }
}

object ProductDao extends ProductDaoT
