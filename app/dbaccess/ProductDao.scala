package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Product

/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait ProductDaoT {

  /**
   * Creates the given user in the database.
   * @param product the user object to be stored.
   * @return the persisted user object
   */
  def addProduct(product: Product): Product = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Products(name, price, size, unit) values ({name}, {price}, {size}, {unit})").on(
          'name -> product.name, 'price -> product.price, 'size -> product.size, 'unit -> product.unit).executeInsert()
      product.id = id.get
    }
    product
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmProduct(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Products where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def availableProducts: List[Product] = {
    DB.withConnection { implicit c =>
      val selectProducts = SQL("Select id, name, price, size, unit from Products;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val products = selectProducts().map(row => Product(row[Long]("id"), row[String]("name"), row[Double]("price"),
        row[Double]("size"), row[String]("unit"))).toList
      products
    }
  }
}

object ProductDao extends ProductDaoT
