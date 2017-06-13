package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import forms._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import com.github.nscala_time.time.Imports._

/**
 * Controller for user specific operations.
  *
  * Import of nscala Library to make time calculations more easily.
  * (C) https://github.com/nscala-time/nscala-time
 *
 * @author ob, scs
 */
object OrderController extends Controller {


  /**
   * Form object for user data.
   */
  val orderForm = Form(
    mapping(
      "userID" -> of(doubleFormat),
      "pizzaID" -> of(doubleFormat),
      "productID" -> of(doubleFormat),
      "pizzaAmount" -> default(number, 0),
      "pizzaSize" -> of(doubleFormat),
      "productAmount" -> default(number, 0),
      "extraOneID" -> of(doubleFormat),
      "extraTwoID" -> of(doubleFormat),
      "extraThreeID" -> of(doubleFormat))(CreateOrderForm.apply)(CreateOrderForm.unapply))

  val modifyOrderForm = Form(
    mapping(
      "orderID" -> of(doubleFormat))(ModifyOrderForm.apply)(ModifyOrderForm.unapply))

  val orderStatusForm = Form(
    mapping(
      "orderID" -> of(longFormat),
      "orderStatusKZ" -> text)(OrderStatusForm.apply)(OrderStatusForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */

  def createOrder : Action[AnyContent] = Action { implicit request =>
    orderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      orderData => {
        //try {
          val selectUser = services.UserService.getUserByID(orderData.userID.toLong)
          var time: Int = (2 * (selectUser.head.distance.toInt / 1000)) + (10 * orderData.pizzaAmount.toInt)
          var deliveryTime = DateTimeFormat.forPattern("kk:mm - DD.MM.YYYY").print(DateTime.now() + time.minutes)



          val orderNew = services.OrderService.createOrder(orderData.userID,
            orderData.pizzaID, orderData.productID, "N/A", "N/A", orderData.pizzaAmount.toDouble, orderData.pizzaSize, -1,
            orderData.productAmount.toDouble, -1,
            orderData.extraOneID, "N/A", 0,
            orderData.extraTwoID, "N/A", 0,
            orderData.extraThreeID, "N/A", 0,
            DateTimeFormat.forPattern("kk:mm - DD.MM.YYYY").print(DateTime.now()), "N/A", deliveryTime)


          val extraTotalPrice = orderNew.extraOnePrice + orderNew.extraTwoPrice + orderNew.extraThreePrice
          val extrasName =
            if(orderNew.extraOneID != 0 && orderNew.extraTwoID == 0 && orderNew.extraThreeID == 0){
              orderNew.extraOneName
            } else if(orderNew.extraOneID != 0 && orderNew.extraTwoID != 0  && orderNew.extraThreeID == 0){
              orderNew.extraOneName + ", " + orderNew.extraTwoName
            } else if (orderNew.extraOneID != 0 && orderNew.extraTwoID != 0 && orderNew.extraThreeID != 0){
              orderNew.extraOneName + ", " + orderNew.extraTwoName + ", " + orderNew.extraThreeName
            }
          Redirect(routes.OrderController.newOrderCreated(orderNew.id, orderNew.customerID, orderNew.pizzaID, orderNew.productID, orderNew.pizzaName, orderNew.productName,
            orderNew.pizzaAmount, orderNew.pizzaSize, orderNew.pizzaPrice, orderNew.productAmount, orderNew.productPrice,
            extrasName.toString, extraTotalPrice, orderNew.totalPrice, orderNew.orderTime, orderNew.status, deliveryTime)).
            flashing("success" -> "Order saved!")
        //} catch {
          //case e: RuntimeException => BadRequest(views.html.orderFailed())
        }
      )
  }

  def setStatusOrder : Action[AnyContent] = Action { implicit request =>
    orderStatusForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.orderFailed())
      },
      orderData => {
        try {
          val newOrder = services.OrderService.orderSetStaus(orderData.orderID, orderData.orderStatusKZ)
          Redirect(routes.EmployeeController.showAllOrderDetails()).
            flashing("success" -> "Order saved!")
        } catch {
          case e: RuntimeException => BadRequest(views.html.orderFailed())
        }
      })
  }

  def showSelectedOrder : Action[AnyContent] = Action { implicit request =>
    modifyOrderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      modifyOrderData => {
        val newOrder = services.OrderService.getOrderbyID(modifyOrderData.orderID)
        val extraTotalPrice = newOrder.extraOnePrice + newOrder.extraTwoPrice + newOrder.extraThreePrice
        val extrasName =

          if(newOrder.extraOneID == 0 && newOrder.extraTwoID == 0 && newOrder.extraThreeID == 0){
            "empty"
          } else if(newOrder.extraOneID != 0 && newOrder.extraTwoID == 0 && newOrder.extraThreeID == 0){
            newOrder.extraOneName
          } else if(newOrder.extraOneID != 0 && newOrder.extraTwoID != 0  && newOrder.extraThreeID == 0){
            newOrder.extraOneName + ", " + newOrder.extraTwoName
          } else if (newOrder.extraOneID != 0 && newOrder.extraTwoID != 0 && newOrder.extraThreeID != 0){
            newOrder.extraOneName + ", " + newOrder.extraTwoName + ", " + newOrder.extraThreeName
          }
        Redirect(routes.OrderController.showOrder(newOrder.customerID, newOrder.pizzaID, newOrder.productID, newOrder.pizzaName, newOrder.productName, newOrder.pizzaAmount, newOrder.pizzaSize, newOrder.pizzaPrice,
          newOrder.productAmount, newOrder.productPrice, newOrder.totalPrice, newOrder.orderTime, newOrder.status, extrasName.toString, extraTotalPrice)).
          flashing("success" -> "Order saved!")
      })
  }

  def deleteOrder : Action[AnyContent] = Action { implicit request =>
    modifyOrderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      modifyOrderData => {
        val newOrder = services.OrderService.rmOrder(modifyOrderData.orderID)
        Redirect(routes.OrderController.orderDeleted()).
          flashing("success" -> "Order saved!")
      })
  }

  def deactivateOrder : Action[AnyContent] = Action { implicit request =>
    modifyOrderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      modifyOrderData => {
        val newOrder = services.OrderService.deactivateOrder(modifyOrderData.orderID)
        Redirect(routes.OrderController.orderDeleted()).
          flashing("success" -> "Order saved!")
      })
  }

  def newOrderCreated(id: Long, customerID: Double, pizzaID: Double, productID: Double,
                      pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                      pizzaPrice: Double, productAmount: Double, productPrice: Double,
                      extrasName: Any, extraTotalPrice: Double, totalPrice: Double,
                      orderTime: String, status: String, deliveryTime: String) : Action[AnyContent] = Action {
    Ok(views.html.newOrder(id.toInt, customerID.toInt, pizzaID.toInt, productID.toInt, pizzaName, productName,
      pizzaAmount, pizzaSize, pizzaPrice, productAmount, productPrice, extrasName.toString,
      extraTotalPrice, totalPrice, orderTime, status, deliveryTime))
  }

  def showOrder(customerID: Double, pizzaID: Double, productID: Double,
                      pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                      pizzaPrice: Double, productAmount: Double, productPrice: Double, totalPrice: Double,
                      orderTime: String, status: String, extrasName: String,
                      extrasTotalPrice: Double) : Action[AnyContent] = Action {

    Ok(views.html.showOrder(customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, totalPrice, orderTime, status, extrasName, extrasTotalPrice))
  }

  /**
   * Shows the welcome view for a newly registered user.
   */


  def orderDeleted : Action[AnyContent] = Action {
    Ok(views.html.orderDeleted())
  }

  /**
   * List all users currently available in the system.
   */

}
