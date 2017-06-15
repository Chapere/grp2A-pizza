package rest

import models.Order
import play.api.libs.json.{JsError, JsValue, Json, Writes}
import play.api.mvc.{Action, AnyContent, BodyParsers, Controller, RequestHeader}
import services.OrderService

/**
 * REST API for the Order Class.
 */
object Orders extends Controller {
  val links = "links"
  val rel = "rel"
  val href = "href"
  val method = "method"
  val self = "self"
  val get = "GET"
  val order = "orders"

  private case class HateoasOrder(order: Order, url: String, customerID: String,
                                  pizzaID: String, productID: String,
                                  pizzaName: String, productName: String,
                                  pizzaAmount: String, pizzaSize: String,
                                  pizzaPrice: String, productAmount: String,
                                  productPrice: String, totalPrice: String,
                                  orderTime: String)

  private def mkHateoasOrder(order: Order)(implicit request: RequestHeader): HateoasOrder = {
    val url = routes.Orders.order(order.id).absoluteURL()
    val customerID = routes.Orders.order(order.id).absoluteURL()
    val pizzaID = routes.Orders.order(order.id).absoluteURL()
    val productID = routes.Orders.order(order.id).absoluteURL()
    val pizzaName = routes.Orders.order(order.id).absoluteURL()
    val productName = routes.Orders.order(order.id).absoluteURL()
    val pizzaAmount = routes.Orders.order(order.id).absoluteURL()
    val pizzaSize = routes.Orders.order(order.id).absoluteURL()
    val pizzaPrice = routes.Orders.order(order.id).absoluteURL()
    val productAmount = routes.Orders.order(order.id).absoluteURL()
    val productPrice = routes.Orders.order(order.id).absoluteURL()
    val totalPrice = routes.Orders.order(order.id).absoluteURL()
    val orderTime = routes.Orders.order(order.id).absoluteURL()

    HateoasOrder(order, url, customerID, pizzaID, productID,
      pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, totalPrice, orderTime)
  }

  private implicit val hateoasOrderWrites = new Writes[HateoasOrder] {
    def writes(horder: HateoasOrder): JsValue = Json.obj(
      order -> Json.obj(
        "id" -> horder.order.id,
        "customerID" -> horder.order.customerID,
        "pizzaID" -> horder.order.pizzaID,
        "productID" -> horder.order.productID,
        "pizzaName" -> horder.order.pizzaName,
        "productName" -> horder.order.productName,
        "pizzaAmount" -> horder.order.pizzaAmount,
        "pizzaSize" -> horder.order.pizzaSize,
        "pizzaPrice" -> horder.order.pizzaPrice,
        "productAmount" -> horder.order.productAmount,
        "productPrice" -> horder.order.productPrice,
        "totalPrice" -> horder.order.totalPrice,
        "orderTime" -> horder.order.orderTime
      ),
      links -> Json.arr(
        Json.obj(
          rel -> self,
          href -> horder.url,
          method -> get
        ),
        Json.obj(
          rel -> "remove",
          href -> horder.url,
          method -> "DELETE"
        )
      )
    )
  }

  /**
   * Get all users.
   * {{{
   * curl --include http://localhost:9000/api/users
   * }}}
   * @return all users in a JSON representation.
   */
  def orders: Action[AnyContent] = Action { implicit request =>
    val orders = OrderService.availableOrder
    Ok(Json.obj(
      order -> Json.toJson(orders.map { order => Json.toJson(mkHateoasOrder(order)) }),
      links -> Json.arr(
        Json.obj(
          rel -> self,
          href -> routes.Orders.orders.absoluteURL(),
          method -> get
        )
      )
    )
    )
  }

  /**
   * Gets a user by id.
   * Use for example
   * {{{
   * curl --include http://localhost:9000/api/user/1
   * }}}
   * @param id user id.
   * @return user info in a JSON representation.
   */
  def order(id: Long): Action[AnyContent] = Action { implicit request =>
    OrderService.availableOrder.find {
      _.id == id
    }.headOption.map { order =>
      Ok(Json.toJson(mkHateoasOrder(order)))
    }.getOrElse(NotFound)
  }

  private case class OrderName(customerID: Double, pizzaID: Double, productID: Double,
                               pizzaName: String, productName: String,
                               pizzaAmount: Double, pizzaSize: Double,
                               pizzaPrice: Double, productAmount: Double,
                               productPrice: Double, totalPrice: Double,
                               orderTime: String, status: String,
                               deliveryTime: String)
  private implicit val OrderReads = Json.reads[OrderName]

}
