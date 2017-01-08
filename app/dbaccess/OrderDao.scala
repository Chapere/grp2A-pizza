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

  def createOrder(order: Order): OrdersWithExtras = {
    DB.withConnection { implicit c =>

      val addOrder: Option[Long] =
        SQL("INSERT INTO ORDERS(customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice, productAmount, productPrice, extraOneID, extraOneName, extraOnePrice, extraTwoID, extraTwoName," +
          " extraTwoPrice, extraThreeID, extraThreeName, extraThreePrice, extrasString, extrasTotalPrice, totalPrice, orderTime, status, deliveryTime) VALUES " +
          "({customerID}, {pizzaID}, {productID}, 'N/A', 'N/A', {pizzaAmount}, {pizzaSize}, 0, {productAmount}, 0, " +
          "{extraOneID}, 'N/A', 0, {extraTwoID}, 'N/A', 0, {extraThreeID}, 'N/A', 0,'N/A', 0, " +
          "0, {orderTime}, {status}, {deliveryTime})").on(
          'customerID -> order.customerID, 'pizzaID -> order.pizzaID, 'productID -> order.productID,
          'pizzaAmount -> order.pizzaAmount, 'pizzaSize -> order.pizzaSize, 'productAmount -> order.productAmount,
          'extraOneID -> order.extraOneID, 'extraTwoID -> order.extraTwoID, 'extraThreeID -> order.extraThreeID,
          'orderTime -> order.orderTime, 'status -> order.status, 'deliveryTime -> order.deliveryTime).executeInsert()

      order.id = addOrder.get

      val selectExtraOne = SQL("SELECT * FROM Extras WHERE id = {extraOneID}").on(
        'extraOneID -> order.extraOneID)
      val getExtraOne = selectExtraOne().map(row => Extra(row[Long]("id"), row[String]("name"), row[Double]("price"))).toList

      val selectExtraTwo = SQL("SELECT * FROM Extras WHERE id = {extraTwoID}").on(
        'extraTwoID -> order.extraTwoID)
      val getExtraTwo = selectExtraTwo().map(row => Extra(row[Long]("id"), row[String]("name"), row[Double]("price"))).toList

      val selectExtraThree = SQL("SELECT * FROM Extras WHERE id = {extraThreeID}").on(
        'extraThreeID -> order.extraThreeID)
      val getExtraThree = selectExtraThree().map(row => Extra(row[Long]("id"), row[String]("name"), row[Double]("price"))).toList

      val selectPizzas = SQL("SELECT * FROM Pizzas WHERE id = {pizzaID}").on(
        'pizzaID -> order.pizzaID)
      val getPizzas = selectPizzas().map(row => Pizza(row[Long]("id"), row[String]("name"), row[Double]("price"),
        row[String]("ingredients"), row[String]("comment"), row[String]("supplements"))).toList

      val selectProducts = SQL("SELECT * FROM Products WHERE id = {productID}").on(
        'productID -> order.productID)
      val getProducts = selectProducts().map(row => Product(row[Long]("id"), row[String]("name"), row[Double]("price"),
        row[Double]("size"), row[String]("unit"))).toList

      val extrasTotalPrice = getExtraOne.head.price + getExtraTwo.head.price +getExtraThree.head.price
      val extrasString = getExtraOne.head.name + " " + getExtraTwo.head.name + " " + getExtraThree.head.name

      val totalPrice = (getPizzas.head.price * order.pizzaAmount * order.pizzaSize) + (order.productAmount * getProducts.head.price) + getExtraOne.head.price + getExtraTwo.head.price + getExtraThree.head.price

      val updateOrder: Double =
        SQL("UPDATE Orders SET pizzaName = {pizzaName}, productName = {productName}, pizzaPrice = {pizzaPrice}, productPrice = {productPrice}, " +
          "extraOneID = {extraOneID}, extraOneName = {extraOneName}, extraOnePrice = {extraOnePrice}, " +
          "extraTwoID = {extraTwoID}, extraTwoName = {extraTwoName}, extraTwoPrice = {extraTwoPrice}, " +
          "extraThreeID = {extraThreeID}, extraThreeName = {extraThreeName}, extraThreePrice = {extraThreePrice}, " +
          "extrasString = {extrasString}, extrasTotalPrice = {extrasTotalPrice}, totalPrice = {totalPrice} WHERE id = {id};").on(
          'pizzaName -> getPizzas.head.name, 'productName -> getProducts.head.name, 'pizzaPrice -> getPizzas.head.price,
          'productPrice -> getProducts.head.price,
          'extraOneID -> getExtraOne.head.id, 'extraOneName -> getExtraOne.head.name, 'extraOnePrice -> getExtraOne.head.price,
          'extraTwoID -> getExtraTwo.head.id, 'extraTwoName -> getExtraTwo.head.name, 'extraTwoPrice -> getExtraThree.head.price,
          'extraThreeID -> getExtraThree.head.id, 'extraThreeName -> getExtraThree.head.name, 'extraThreePrice -> getExtraThree.head.price,
          'extrasString -> extrasString.toString, 'extrasTotalPrice -> extrasTotalPrice,
          'totalPrice -> totalPrice, 'id -> order.id).executeUpdate()

      val selectOrder = SQL("SELECT * FROM Orders WHERE id = {id};").on(
        'id -> order.id)
      val orders = selectOrder().map(row => OrdersWithExtras(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("extraOneID"), row[String]("extraOneName"), row[Double]("extraOnePrice"),
        row[Double]("extraTwoID"), row[String]("extraTwoName"), row[Double]("extraTwoPrice"),
        row[Double]("extraThreeID"), row[String]("extraThreeName"), row[Double]("extraThreePrice"),
        row[String]("extrasString"), row[Double]("extrasTotalPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[String]("deliveryTime"))).toList

      orders.head

    }

  }

  def setOrderStatus(id: Long, orderStatus: String): Order = {
    DB.withConnection { implicit c =>

      val updateFlag: Option[Long] =
        SQL("UPDATE Orders SET status = {status} WHERE id = {id}").on(
          'status -> orderStatus, 'id -> id).executeInsert()

      val selectOrder = SQL("SELECT * FROM Orders WHERE id = {id};").on(
        'id -> id)
      val orders = selectOrder().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("extraOneID"), row[String]("extraOneName"), row[Double]("extraOnePrice"),
        row[Double]("extraTwoID"), row[String]("extraTwoName"), row[Double]("extraTwoPrice"),
        row[Double]("extraThreeID"), row[String]("extraThreeName"), row[Double]("extraThreePrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[String]("deliveryTime"))).toList

      return orders.head
    }

  }

  def getOrder(id: Double): Order = {
    DB.withConnection { implicit c =>
      val selectOrder = SQL("SELECT * FROM Orders WHERE id = {id};").on(
        'id -> id)
      val orders = selectOrder().map(row => Order(row[Long]("id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("extraOneID"), row[String]("extraOneName"), row[Double]("extraOnePrice"),
        row[Double]("extraTwoID"), row[String]("extraTwoName"), row[Double]("extraTwoPrice"),
        row[Double]("extraThreeID"), row[String]("extraThreeName"), row[Double]("extraThreePrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[String]("deliveryTime"))).toList

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

  def deactivateOrder(id: Double): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("UPDATE Orders SET status = {status} WHERE id = {id}").on(
      'status -> "Storniert", 'id -> id).executeInsert()
      true
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
        row[Double]("extraOneID"), row[String]("extraOneName"), row[Double]("extraOnePrice"),
        row[Double]("extraTwoID"), row[String]("extraTwoName"), row[Double]("extraTwoPrice"),
        row[Double]("extraThreeID"), row[String]("extraThreeName"), row[Double]("extraThreePrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[String]("deliveryTime"))).toList
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
        row[Double]("extraOneID"), row[String]("extraOneName"), row[Double]("extraOnePrice"),
        row[Double]("extraTwoID"), row[String]("extraTwoName"), row[Double]("extraTwoPrice"),
        row[Double]("extraThreeID"), row[String]("extraThreeName"), row[Double]("extraThreePrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[String]("deliveryTime"))).toList
      orders
    }
  }

  def availableOrdersWithAdress: List[OrderWithAdress] = {
    DB.withConnection { implicit c =>
      val selectOrders = SQL("Select Orders.*, Users.* from Orders LEFT JOIN Users ON Orders.customerID = Users.id;")

      // Transform the resulting Stream[Row] to a List[(String,String)]
      val orders = selectOrders().map(row => OrderWithAdress(row[Long]("Orders.id"), row[Double]("customerID"), row[Double]("pizzaID"),
        row[Double]("productID"), row[String]("pizzaName"), row[String]("productName"), row[Double]("pizzaAmount"),
        row[Double]("pizzaSize"), row[Double]("pizzaPrice"), row[Double]("productAmount"), row[Double]("productPrice"),
        row[Double]("totalPrice"), row[String]("orderTime"), row[String]("status"), row[Long]("Users.id"), row[String]("name"), row[String]("lastname"),
        row[String]("adress"), row[String]("city"), row[String]("plz"), row[Double]("distance"), row[String]("email"), null, row[Int]("activeFlag"),
        row[String]("extrasString"), row[Double]("extrasTotalPrice"), row[String]("deliveryTime"))).toList

      orders
    }
  }

}

object OrderDao extends OrderDaoT
