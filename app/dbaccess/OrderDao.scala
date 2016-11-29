package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Order

/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait OrderDaoT {

  /**
   * Creates the given user in the database.
   * @param order the user object to be stored.
   * @return the persisted user object
   */
  def addOrder(order: Order): Order = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Orders(customerID, produktID, ammount, extras, price, orderTime) values ({customerID}, {produktID}, {ammount}, {extras}, {price}, {orderTime})").on(
          'customerID -> order.customerID, 'produktID -> order.produktID, 'ammount -> order.ammount, 'extras -> order.extras, 'price -> order.price, 'orderTime -> order.orderTime).executeInsert()
      order.id = id.get
    }
    order
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmOrder(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Orders where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def availableOrders: List[Order] = {
    DB.withConnection { implicit c =>
      val selectOrders = SQL("Select id, name, price, ingredients, comment, supplements from Orders;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val orders = selectOrders().map(row => Order(row[Long]("id"), row[String]("customerID"), row[String]("produktID"),
        row[String]("ammount"), row[String]("extras"), row[String]("price"), row[String]("orderTime"))).toList
      orders
    }
  }
}

object OrderDao extends OrderDaoT
