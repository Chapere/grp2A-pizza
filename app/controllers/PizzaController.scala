package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, of}
import services.{PizzaService, ProductService, ExtraService}
import forms.{CreatePizzaForm, IDForm}
import play.api.data.format.Formats.{longFormat, doubleFormat}

/**
  * Controller for pizza specific operations.
  *
  * @author ob, scs
  */
object PizzaController extends Controller {
  val id = "id"
  val name = "Name"
  val price = "Preis pro cm"
  val zutaten = "Zutaten"
  val kommentar = "Kommentar"
  val zusatzstoffe = "Zusatzstoffe"
  val success = "success"
  val pizzaSaved = "Pizza saved!"

  /**
    * Form object for pizza data.
    */
  val pizzaForm = Form(
    mapping(
      id -> of(longFormat),
      name -> nonEmptyText,
      price -> of(doubleFormat),
      zutaten -> nonEmptyText,
      kommentar -> nonEmptyText,
      zusatzstoffe -> nonEmptyText)(CreatePizzaForm.apply)(CreatePizzaForm.unapply))

  /**
    * Form object for retrieving pizza data from the database.
    */
  val selectPizzaForm = Form(
    mapping(
      id -> of(longFormat))(IDForm.apply)(IDForm.unapply))

  /**
    * Adds a new pizza with the given data to the database.
    *
    * @return page for a new pizza
    */
  def addPizza: Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      pizzaData => {
        val newPizza = services.PizzaService.addPizza(pizzaData.name,
          pizzaData.price, pizzaData.ingredients,
          pizzaData.comment, pizzaData.supplements)
        Redirect(routes.PizzaController.newPizzaCreated(newPizza.id,
          newPizza.name, newPizza.price, newPizza.ingredients,
          newPizza.comment, newPizza.supplements)).
          flashing(success -> pizzaSaved)
      })
  }

  /**
    * Changes an already existing pizza in the database.
    *
    * @return page for a changed pizza
    */
  def updatePizza: Action[AnyContent] = Action { implicit request =>
    pizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      pizzaData => {
        val selectPizza = services.PizzaService.updatePizza(pizzaData.id,
          pizzaData.name, pizzaData.price, pizzaData.ingredients,
          pizzaData.comment, pizzaData.supplements)
        Redirect(routes.PizzaController.upgradePizza(selectPizza.id,
          selectPizza.name, selectPizza.price, selectPizza.ingredients,
          selectPizza.comment, selectPizza.supplements)).
          flashing(success -> "Employee saved!")
      })
  }

  /**
    * Removes/deactivates a pizza.
    *
    * @return page for a deleted pizza
    */
  def rmPizza: Action[AnyContent] = Action { implicit request =>
    selectPizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deletePizzaData => {
        val deletePizzaVal = services.PizzaService.rmPizza(deletePizzaData.id)
        Redirect(routes.PizzaController.pizzaDeleted(deletePizzaVal)).
          flashing(success -> pizzaSaved)
      })
  }

  /**
    * Retrieves a pizza from the database.
    *
    * @return page to change a pizza
    */
  def getPizza: Action[AnyContent] = Action { implicit request =>
    selectPizzaForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectPizzaData => {
        val selectPizza = services.PizzaService.selectPizza(selectPizzaData.id)
        Redirect(routes.PizzaController.changePizza1(selectPizza.id,
          selectPizza.name, selectPizza.price, selectPizza.ingredients,
          selectPizza.comment, selectPizza.supplements)).
          flashing(success -> pizzaSaved)
      })
  }

  /**
    * Shows the view for a newly created pizza.
    */
  def newPizzaCreated(id: Long, name: String, price: Double,
                      ingredients: String, comment: String,
                      supplements: String): Action[AnyContent] = Action {
    Ok(views.html.newPizzaCreated(id, name, price,
      ingredients, comment, supplements))
  }

  /**
    * Shows the view to change a pizza.
    */
  def changePizza1(id: Long, name: String, price: Double,
                   ingredients: String, comment: String,
                   supplements: String): Action[AnyContent] = Action {
    Ok(views.html.changePizza(id, name, price, ingredients,
      comment, supplements, pizzaForm))
  }

  /**
    * Shows the view for a changed pizza.
    */
  def upgradePizza(id: Long, name: String, price: Double,
                   ingredients: String, comment: String,
                   supplements: String): Action[AnyContent] = Action {
    Ok(views.html.pizzaUpdated(id, name, price,
      ingredients, comment, supplements))
  }

  /**
    * Shows the view for a deleted pizza.
    */
  def pizzaDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.pizzaDeleted())
  }

  /**
    * List all pizzas currently available in the database.
    */
  def showPizzas: Action[AnyContent] = Action {
    Ok(views.html.allPizzas(PizzaService.availablePizza))
  }

  /**
    * List all products currently available in the database.
    */
  def products = Action { request =>
    request.session.get("loggedInUser").map { userID =>
      Ok(views.html.products(PizzaService.availablePizza,
        ProductService.availableProducts, OrderController.orderForm,
        userID.toDouble, ExtraService.availableExtras))
    }.getOrElse {
      Ok(views.html.products(PizzaService.availablePizza,
        ProductService.availableProducts,
        OrderController.orderForm, -1, ExtraService.availableExtras))
    }
  }

}
