package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import play.api.data.Form
import play.api.data.Forms.{mapping, of, nonEmptyText}
import services.ExtraService
import forms.{IDForm, CreateExtraForm}
import play.api.data.format.Formats.{longFormat, doubleFormat}

/**
  * Controller for extra specific operations.
  *
  * GoogleMaps API KEY = AIzaSyC6VCdDfHl2b9yRYnJnMq2PgSjPoXlEXow
  *
  * @author ob, scs
  */
object ExtraController extends Controller {
  val id1 = "id"
  val success = "success"

  /**
    * Form object for extra data.
    */
  val extraForm = Form(
    mapping(
      id1 -> of(longFormat),
      "Name" -> nonEmptyText,
      "Preis" -> of(doubleFormat))(CreateExtraForm.apply)(CreateExtraForm.unapply))

  /**
    * Form object for retrieving extra data from the database.
    */
  val selectExtraForm = Form(
    mapping(
      id1 -> of(longFormat))(IDForm.apply)(IDForm.unapply))


  /**
    * Adds a new extra with the given data to the database.
    *
    * @return page for a new extra
    */
  def addExtra: Action[AnyContent] = Action { implicit request =>
    extraForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      extraData => {
        val newExtra = services.ExtraService.addExtra(extraData.name,
          extraData.price)
        Redirect(routes.ExtraController.newExtraCreated(newExtra.id,
          newExtra.name, newExtra.price)).
          flashing(success -> "Extra saved!")
      })
  }

  /**
    * Changes an already existing extra in the database.
    *
    * @return page for a changed extra
    */
  def updateExtra: Action[AnyContent] = Action { implicit request =>
    extraForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      extraData => {
        val selectExtra = services.ExtraService.updateExtra(extraData.id,
          extraData.name, extraData.price)
        Redirect(routes.ExtraController.upgradeExtra(selectExtra.id,
          selectExtra.name, selectExtra.price)).
          flashing(success -> "Extra updated!")
      })
  }

  /**
    * Removes/deactivates an extra.
    *
    * @return page for a deleted extra
    */
  def rmExtra: Action[AnyContent] = Action { implicit request =>
    selectExtraForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deleteExtraData => {
        val deleteExtraVal = services.ExtraService.rmExtra(deleteExtraData.id)
        Redirect(routes.ExtraController.extraDeleted(deleteExtraVal)).
          flashing(success -> "Extra deleted!")
      })
  }

  /**
    * Retrieves an extra from the database.
    *
    * @return page to change an extra
    */
  def getExtra: Action[AnyContent] = Action { implicit request =>
    selectExtraForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectExtraData => {
        val selectExtra = services.ExtraService.selectExtra(selectExtraData.id)
        Redirect(routes.ExtraController.changeExtra1(selectExtra.id,
          selectExtra.name, selectExtra.price)).
          flashing(success -> "Extras retrieved!")
      })
  }


  /**
    * Shows the view for a newly created extra.
    *
    * @param id    the id of an extra
    * @param name  the name of an extra
    * @param price the price of an extra
    */
  def newExtraCreated(id: Long, name: String, price: Double): Action[AnyContent] = Action {
    Ok(views.html.newExtraCreated(id, name, price))
  }

  /**
    * Shows the view to change an extra.
    *
    * @param id    the id of an extra
    * @param name  the name of an extra
    * @param price the price of an extra
    */
  def changeExtra1(id: Long, name: String, price: Double): Action[AnyContent] = Action {
    Ok(views.html.changeExtra(id, name, price, extraForm))
  }

  /**
    * Shows the view for a changed extra.
    *
    * @param id    the id of an extra
    * @param name  the name of an extra
    * @param price the price of an extra
    */
  def upgradeExtra(id: Long, name: String, price: Double): Action[AnyContent] = Action {
    Ok(views.html.extraUpdated(id, name, price))
  }

  /**
    * Shows the view for a deleted extra.
    *
    * @param deleted shows whether or not an extra has been deleted
    */
  def extraDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.extraDeleted())
  }

  /**
    * List all extras currently available in the database.
    */
  def showExtras: Action[AnyContent] = Action {
    Ok(views.html.allExtras(ExtraService.availableExtras))
  }
}

