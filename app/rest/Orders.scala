package rest

import models.Order
import play.api.libs.json.{JsError, JsValue, Json, Writes}
import play.api.mvc._
import services.OrderService

/**
 * REST API for the Order Class.
 */
object Orders extends Controller {

  private case class HateoasOrder(order: Order, url: String, amount: String, extras: String, orderTime: String)

  private def mkHateoasOrder(order: Order)(implicit request: RequestHeader): HateoasOrder = {
    val url = routes.Orders.order(order.id).absoluteURL()
    val amount = routes.Orders.order(order.id).absoluteURL()
    val extras = routes.Orders.order(order.id).absoluteURL()
    val orderTime = routes.Orders.order(order.id).absoluteURL()

    HateoasOrder(order, url, amount, extras, orderTime)
  }

  private implicit val hateoasOrderWrites = new Writes[HateoasOrder] {
    def writes(horder: HateoasOrder): JsValue = Json.obj(
      "order" -> Json.obj(
        "id" -> horder.order.id,
        "customerID" -> horder.order.customerID,
        "produktID" -> horder.order.produktID,
        "amount" -> horder.order.amount,
        "extras" -> horder.order.extras,
        "price" -> horder.order.price,
        "orderTime" -> horder.order.orderTime
      ),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> horder.url,
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "remove",
          "href" -> horder.url,
          "method" -> "DELETE"
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
      "orders" -> Json.toJson(orders.map { order => Json.toJson(mkHateoasOrder(order)) }),
      "links" -> Json.arr(
        Json.obj(
          "rel" -> "self",
          "href" -> routes.Orders.orders.absoluteURL(),
          "method" -> "GET"
        ),
        Json.obj(
          "rel" -> "create",
          "href" -> routes.Orders.addOrder.absoluteURL(),
          "method" -> "POST"
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

  private case class OrderName(customerID: Int, produktID: Int, amount: Int, extras: String, price: Double, orderTime: String)
  private implicit val OrderReads = Json.reads[OrderName]

  /**
   * Create a new user by a POST request including the user name as JSON content.
   * Use for example
   * {{{
   * curl --include --request POST --header "Content-type: application/json"
   *      --data '{"name" : "WieAuchImmer"}' http://localhost:9000/api/user
   * }}}
   * @return info about the new user in a JSON representation
   */
  def addOrder: Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val ordername = request.body.validate[OrderName]
    ordername.fold(
      errors => {
        BadRequest(Json.obj("status" -> "OK", "message" -> JsError.toFlatJson(errors)))
      },
      ordername => {
        Ok(Json.obj("status" -> "OK",
          "order" -> Json.toJson(mkHateoasOrder(OrderService.addOrder(ordername.customerID, ordername.produktID, ordername.amount, ordername.extras, ordername.price, ordername.orderTime)))))
      }
    )
  }

  /**
   * Delete a user by id using a DELETE request.
   * {{{
   * curl --include --request DELETE http://localhost:9000/api/user/1
   * }}}
   * @param id the user id.
   * @return success info or NotFound
   */
  def rmOrder(id: Long): Action[AnyContent] = Action { implicit request =>
    val success = OrderService.rmOrder(id)
    if (success)
      Ok(Json.obj("status" -> "OK"))
    else
      NotFound
  }
}
