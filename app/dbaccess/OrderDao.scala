package dbaccess

import anorm._
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models._

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

  def createOrder(order: Order): Order = {
    DB.withConnection { implicit c =>

      val addOrder: Option[Long] =
        SQL("INSERT INTO ORDERS(customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice, productAmount, productPrice, totalPrice, orderTime, status) VALUES " +
          "({customerID}, {pizzaID}, {productID}, 'N/A', 'N/A', {pizzaAmount}, {pizzaSize}, 0, {productAmount}, 0, 0, {orderTime}, {status})").on(
          'customerID -> order.customerID, 'pizzaID -> order.pizzaID, 'productID -> order.productID, 'pizzaAmount -> order.pizzaAmount, 'pizzaSize -> order.pizzaSize, 'productAmount -> order.productAmount, 'orderTime -> order.orderTime, 'status -> order.status).executeInsert()

      order.id = addOrder.get


      val selectPizzas = SQL("SELECT * FROM Pizzas WHERE id = {pizzaID}").on(
        'pizzaID -> order.pizzaID)
      val getPizzas = selectPizzas().map(row => Pizza(row[Long]("id"), row[String]("name"), row[Double]("price"),
        row[String]("ingredients"), row[String]("comment"), row[String]("supplements"))).toList

      val selectProducts = SQL("SELECT * FROM Products WHERE id = {productID}").on(
        'productID -> order.productID)
      val getProducts = selectProducts().map(row => Product(row[Long]("id"), row[String]("name"), row[Double]("price"),
        row[Double]("size"), row[String]("unit"))).toList

      val totalPrice = (getPizzas.head.price * order.pizzaAmount * order.pizzaSize) + (order.productAmount * getProducts.head.price)

      val updateOrder: Double =
        SQL("UPDATE Orders SET pizzaName = {pizzaName}, productName = {productName}, pizzaPrice = {pizzaPrice}, productPrice = {productPrice}, totalPrice = {totalPrice} WHERE id = {id};").on(
          'pizzaName -> getPizzas.head.name, 'productName -> getProducts.head.name, 'pizzaPrice -> getPizzas.head.price, 'productPrice -> getProducts.head.price, 'totalPrice -> totalPrice, 'id -> order.id).executeUpdate()

      val selectOrder = SQL("SELECT * FROM Orders WHERE id = {id};").on(
        'id -> order.id)
      val orders = selectOrder().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"))).toList

      orders.head

    }

  }

  def getOrder(id: Double): Order = {
    DB.withConnection { implicit c =>
      val selectOrder = SQL("SELECT * FROM Orders WHERE id = {id};").on(
        'id -> id)
      val orders = selectOrder().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"))).toList

      orders.head
    }
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmOrder(id: Double): Boolean = {
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
      val selectOrders = SQL("Select * from Orders;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val orders = selectOrders().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"))).toList
      orders
    }
  }

  def availableOrdersByID(id: Double): List[Order] = {
    DB.withConnection { implicit c =>
      val selectOrders = SQL("Select * from Orders WHERE customerID = {id};").on('id -> id)
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val orders = selectOrders().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"))).toList
      orders
    }
  }

  def availableOrdersWithAdress: List[OrderWithAdress] = {
    DB.withConnection { implicit c =>
      val selectOrders = SQL("Select Orders.*, Users.* from Orders LEFT JOIN Users ON Orders.customerID = Users.id;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val orders = selectOrders().map(row => OrderWithAdress(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[Long]("id"), row[String]("name"), row[String]("lastname"),
        row[String]("adress"), row[String]("city"), row[String]("plz"), row[Double]("distance"), row[String]("email"), null, row[Int]("activeFlag"))).toList

      orders
    }
  }
}

object OrderDao extends OrderDaoT
