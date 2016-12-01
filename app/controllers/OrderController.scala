package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import services.{OrderService, PizzaService}
import forms.CreateOrderForm
import play.api.data.Form
import play.api.data.Forms.{mapping, text, number}

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object OrderController extends Controller {


  /**
   * Form object for user data.
   */
  val orderForm = Form(
    mapping(
      "Bestellnummer" -> number, "Anzahl" -> number)(CreateOrderForm.apply)(CreateOrderForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def createOrder : Action[AnyContent] = Action { implicit request =>
    orderForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.products(PizzaService.availablePizza, formWithErrors))
      },
      orderData => {
        val newOrder = services.OrderService.createOrder(models.activeUser.id, orderData.orderNumber, orderData.amount, "N/A", 0, "N/A")
        Redirect(routes.OrderController.newOrderCreated(newOrder)).
          flashing("success" -> "Order saved!")
      })
  }

  def newOrderCreated(price: Double) : Action[AnyContent] = Action {
    Ok(views.html.newOrderCreated(price))
  }

  /**
   * Shows the welcome view for a newly registered user.
   */

  def orders : Action[AnyContent] = Action {
    Ok(views.html.orders(OrderService.availableOrder))
  }

  /**
   * List all users currently available in the system.
   */

}
