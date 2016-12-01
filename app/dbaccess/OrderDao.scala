package dbaccess

import anorm.{SQL, SqlParser}
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.{Order, User}

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
        SQL("insert into Orders(customerID, produktID, amount, extras, price, orderTime) values ({customerID}, {produktID}, {amount}, {extras}, {price}, {orderTime})").on(
          'customerID -> order.customerID, 'produktID -> order.produktID, 'amount -> order.amount, 'extras -> order.extras, 'price -> order.price, 'orderTime -> order.orderTime).executeInsert()
      order.id = id.get
    }
    order
  }

  def createOrder(order: Order): Double = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Orders(customerID, produktID, amount, extras, price, orderTime) values ({customerID}, {produktID}, {amount}, {extras}, {price}, {orderTime})").on(
          'customerID -> order.customerID, 'produktID -> order.produktID, 'amount -> order.amount, 'extras -> order.extras, 'price -> order.price,  'orderTime -> order.orderTime).executeInsert()

      order.id = id.get

      val price: Double =
      SQL("SELECT Orders.produktID, Orders.amount, Orders.extras, Orders.orderTime, Pizzas.id, Pizzas.name, Pizzas.price FROM Orders LEFT JOIN Pizzas ON Orders.produktID = Pizzas.id LIMIT 1").
        as(SqlParser.double("price").single)

      val newprice = price * order.amount

      models.Warenkorb.amount = order.amount
      models.Warenkorb.price = price
      models.Warenkorb.priceEnd = newprice
      models.Warenkorb.produktID = order.produktID
      models.Warenkorb.customerID = order.customerID

      val name: String =
        SQL("SELECT Orders.produktID, Orders.amount, Orders.extras, Orders.orderTime, Pizzas.id, Pizzas.name, Pizzas.price FROM Orders LEFT JOIN Pizzas ON Orders.produktID = Pizzas.id LIMIT 1").
          as(SqlParser.str("name").single)
      models.Warenkorb.product = name

      newprice

    }

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
      val orders = selectOrders().map(row => Order(row[Long]("id"), row[Int]("customerID"), row[Int]("produktID"),
        row[Int]("amount"), row[String]("extras"), row[Double]("price"), row[String]("orderTime"))).toList
      orders
    }
  }
}

object OrderDao extends OrderDaoT
