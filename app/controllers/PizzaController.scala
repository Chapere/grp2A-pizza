package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms._
import services._
import forms._
import play.api.data.format.Formats._

/**
 * Controller for user specific operations.
 *
 * @author ob, scs
 */
object PizzaController extends Controller {


  /**
   * Form object for user data.
   */
  val pizzaForm = Form(
    mapping(
      "Name" -> nonEmptyText,
      "Preis" -> of(doubleFormat),
      "Zutaten" -> nonEmptyText,
      "Kommentar" -> nonEmptyText,
      "Zusatzstoffe" -> nonEmptyText)(CreatePizzaForm.apply)(CreatePizzaForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addPizza : Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.products(formWithErrors, pizzas: List[models.Pizza], products: List[models.Product]))

      },
      pizzaData => {
        val newPizza = services.PizzaService.addPizza(pizzaData.name, pizzaData.price, pizzaData.ingredients, pizzaData.comment, pizzaData.supplements)
        Redirect(routes.PizzaController.newPizzaCreated(newPizza.name, newPizza.price, newPizza.ingredients, newPizza.comment, newPizza.supplements)).
          flashing("success" -> "Pizza saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */

  def products = Action { request =>
    request.session.get("loggedInUser").map { userID =>
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, OrderController.orderForm, userID.toDouble))
    }.getOrElse {
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProduct, OrderController.orderForm, -1))
    }
  }

  /**
   * List all users currently available in the system.
   */

  def newPizzaCreated(name: String, price: Double, ingredients: String, comment: String, supplements: String): Action[AnyContent] = Action {
    Ok(views.html.newPizzaCreated(name, price, ingredients, comment, supplements))
  }

}
