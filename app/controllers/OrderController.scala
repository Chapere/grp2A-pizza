package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services._
import forms._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formats._
import java.util.Calendar
import java.text.SimpleDateFormat

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object OrderController extends Controller {

  val today = Calendar.getInstance.getTime
  val curTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
  val today1 = Calendar.getInstance().getTime()


  /**
   * Form object for user data.
   */
  val orderForm = Form(
    mapping(
      "pizzaID" -> of(doubleFormat),
      "productID" -> of(doubleFormat),
      "pizzaAmount" -> of(doubleFormat),
      "pizzaSize" -> of(doubleFormat),
      "productAmount" -> of(doubleFormat))(CreateOrderForm.apply)(CreateOrderForm.unapply))

  val modifyOrderForm = Form(
    mapping(
      "orderID" -> of(doubleFormat))(ModifyOrderForm.apply)(ModifyOrderForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */

  def createOrder : Action[AnyContent] = Action { implicit request =>
    orderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, formWithErrors, 1))
      },
      orderData => {
        try {
          val newOrder = services.OrderService.createOrder(models.activeUser.id,
            orderData.pizzaID, orderData.productID, "N/A", "N/A", orderData.pizzaAmount, orderData.pizzaSize, -1,
            orderData.productAmount, -1, curTimeFormat.format(today), "N/A")
          Redirect(routes.OrderController.newOrderCreated(newOrder.customerID, newOrder.pizzaID, newOrder.productID, newOrder.pizzaName, newOrder.productName, newOrder.pizzaAmount, newOrder.pizzaSize, newOrder.pizzaPrice,
            newOrder.productAmount, newOrder.productPrice, newOrder.totalPrice, newOrder.orderTime, newOrder.status)).
            flashing("success" -> "Order saved!")
        } catch {
          case e: RuntimeException => BadRequest(views.html.orderFailed())
        }
      })
  }

  def showSelectedOrder : Action[AnyContent] = Action { implicit request =>
    modifyOrderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, orderForm, 1))
      },
      modifyOrderData => {
        val newOrder = services.OrderService.getOrderbyID(modifyOrderData.orderID)
        Redirect(routes.OrderController.showOrder(newOrder.customerID, newOrder.pizzaID, newOrder.productID, newOrder.pizzaName, newOrder.productName, newOrder.pizzaAmount, newOrder.pizzaSize, newOrder.pizzaPrice,
          newOrder.productAmount, newOrder.productPrice, newOrder.totalPrice, newOrder.orderTime, newOrder.status)).
          flashing("success" -> "Order saved!")
      })
  }

  def deleteOrder : Action[AnyContent] = Action { implicit request =>
    modifyOrderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, orderForm, 1))
      },
      modifyOrderData => {
        val newOrder = services.OrderService.rmOrder(modifyOrderData.orderID)
        Redirect(routes.OrderController.orderDeleted()).
          flashing("success" -> "Order saved!")
      })
  }

  def newOrderCreated(customerID: Double, pizzaID: Double, productID: Double,
                      pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                      pizzaPrice: Double, productAmount: Double, productPrice: Double, totalPrice: Double,
                      orderTime: String, status: String) : Action[AnyContent] = Action {
    Ok(views.html.newOrderCreated(customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, totalPrice, orderTime, status))
  }

  def showOrder(customerID: Double, pizzaID: Double, productID: Double,
                      pizzaName: String, productName: String, pizzaAmount: Double, pizzaSize: Double,
                      pizzaPrice: Double, productAmount: Double, productPrice: Double, totalPrice: Double,
                      orderTime: String, status: String) : Action[AnyContent] = Action {
    Ok(views.html.showOrder(customerID, pizzaID, productID, pizzaName, productName, pizzaAmount, pizzaSize, pizzaPrice,
      productAmount, productPrice, totalPrice, orderTime, status))
  }

  /**
   * Shows the welcome view for a newly registered user.
   */

  def orders : Action[AnyContent] = Action {
    Ok(views.html.orders(OrderService.availableOrder))
  }

  def orderDeleted : Action[AnyContent] = Action {
    Ok(views.html.orderDeleted())
  }

  /**
   * List all users currently available in the system.
   */

}
