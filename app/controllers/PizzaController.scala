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
      "id" -> of(longFormat),
      "Name" -> nonEmptyText,
      "Preis pro cm" -> of(doubleFormat),
      "Zutaten" -> nonEmptyText,
      "Kommentar" -> nonEmptyText,
      "Zusatzstoffe" -> nonEmptyText)(CreatePizzaForm.apply)(CreatePizzaForm.unapply))


  val selectPizzaForm = Form(
    mapping(
      "id" -> of(longFormat))(IDForm.apply)(IDForm.unapply))

  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addPizza : Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      pizzaData => {
        val newPizza = services.PizzaService.addPizza(pizzaData.name, pizzaData.price, pizzaData.ingredients, pizzaData.comment, pizzaData.supplements)
        Redirect(routes.PizzaController.newPizzaCreated(newPizza.id, newPizza.name, newPizza.price, newPizza.ingredients, newPizza.comment, newPizza.supplements)).
          flashing("success" -> "Pizza saved!")
      })
  }

  def updatePizza: Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      pizzaData => {
        val selectPizza = services.PizzaService.updatePizza(pizzaData.id, pizzaData.name, pizzaData.price, pizzaData.ingredients, pizzaData.comment, pizzaData.supplements)
        Redirect(routes.PizzaController.upgradePizza(selectPizza.id, selectPizza.name, selectPizza.price, selectPizza.ingredients, selectPizza.comment, selectPizza.supplements)).
          flashing("success" -> "Employee saved!")
      })
  }

  def rmPizza: Action[AnyContent] = Action { implicit request =>
    selectPizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deletePizzaData => {
        val deletePizzaVal = services.PizzaService.rmPizza(deletePizzaData.id)
        Redirect(routes.PizzaController.pizzaDeleted(deletePizzaVal)).
          flashing("success" -> "Pizza saved!")
      })
  }


  def getPizza: Action[AnyContent] = Action { implicit request =>
    selectPizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectPizzaData => {
        val selectPizza = services.PizzaService.selectPizza(selectPizzaData.id)
        Redirect(routes.PizzaController.changePizza1(selectPizza.id, selectPizza.name, selectPizza.price, selectPizza.ingredients, selectPizza.comment, selectPizza.supplements)).
          flashing("success" -> "Pizza saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */

  def newPizzaCreated(id: Long, name: String, price: Double, ingredients: String, comment: String, supplements: String): Action[AnyContent] = Action {
    Ok(views.html.newPizzaCreated(id, name, price, ingredients, comment, supplements))
  }

  def changePizza1(id: Long, name: String, price: Double, ingredients: String, comment: String, supplements: String): Action[AnyContent] = Action {
    Ok(views.html.changePizza(id, name, price, ingredients, comment, supplements, pizzaForm))
  }

  def upgradePizza(id: Long, name: String, price: Double, ingredients: String, comment: String, supplements: String): Action[AnyContent] = Action {
    Ok(views.html.pizzaUpdated(id, name, price, ingredients, comment, supplements))
  }

  def pizzaDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.pizzaDeleted())
  }



  /**
   * List all users currently available in the system.
   */

  def showPizzas : Action[AnyContent] = Action {
    Ok(views.html.allPizzas(PizzaService.availablePizza))
  }

  def products = Action { request =>
    request.session.get("loggedInUser").map { userID =>
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProducts, OrderController.orderForm, userID.toDouble, ExtraService.availableExtras))
    }.getOrElse {
      Ok(views.html.products(PizzaService.availablePizza, ProductService.availableProducts, OrderController.orderForm, -1, ExtraService.availableExtras))
    }
  }

}
