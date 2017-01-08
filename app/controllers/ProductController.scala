package controllers

import play.api.mvc._
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
object ProductController extends Controller {


  /**
   * Form object for user data.
   */
  val productForm = Form(
    mapping(
      "id" -> of(longFormat),
      "Name" -> nonEmptyText,
      "Preis" -> of(doubleFormat),
      "Größe" -> of(doubleFormat),
      "Einheit" -> nonEmptyText)(CreateProductForm.apply)(CreateProductForm.unapply))


  val selectProductForm = Form(
    mapping(
      "id" -> of(longFormat))(IDForm.apply)(IDForm.unapply))
  /**
   * Adds a new user with the given data to the system.
   *
   * @return welcome page for new user
   */
  def addProduct: Action[AnyContent] = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      productData => {
        val newProduct = services.ProductService.addProduct(productData.name, productData.price, productData.size, productData.unit)
        Redirect(routes.ProductController.newProductCreated(newProduct.id, newProduct.name, newProduct.price, newProduct.size, newProduct.unit)).
          flashing("success" -> "Product saved!")
      })
  }

  def updateProduct: Action[AnyContent] = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      productData => {
        val selectProduct = services.ProductService.updateProduct(productData.id, productData.name, productData.price, productData.size, productData.unit)
        Redirect(routes.ProductController.upgradeProduct(selectProduct.id, selectProduct.name, selectProduct.price, selectProduct.size, selectProduct.unit)).
          flashing("success" -> "Employee saved!")
      })
  }


  def rmProduct: Action[AnyContent] = Action { implicit request =>
    selectProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deleteProductData => {
        val deleteProductVal = services.ProductService.rmProduct(deleteProductData.id)
        Redirect(routes.ProductController.productDeleted(deleteProductVal)).
          flashing("success" -> "Product saved!")
      })
  }


  def getProduct: Action[AnyContent] = Action { implicit request =>
    selectProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectProductData => {
        val selectProduct = services.ProductService.selectProduct(selectProductData.id)
        Redirect(routes.ProductController.changeProduct1(selectProduct.id, selectProduct.name, selectProduct.price, selectProduct.size, selectProduct.unit)).
          flashing("success" -> "Product saved!")
      })
  }

  /**
   * Shows the welcome view for a newly registered user.
   */

  def newProductCreated(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.newProductCreated(id, name, price, size, unit))
  }

  def changeProduct1(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.changeProduct(id, name, price, size, unit, productForm))
  }

  def upgradeProduct(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.productUpdated(id, name, price, size, unit))
  }

  def productDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.productDeleted())
  }

  /**
    * List all products currently available in the system.
    */
  def showProducts: Action[AnyContent] = Action {
    Ok(views.html.allProducts(ProductService.availableProducts))
  }

  /**
   * List all users currently available in the system.
   */

}
