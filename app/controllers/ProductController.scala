package controllers

import play.api.mvc.{Controller, Action, AnyContent}
import play.api.data.Form
import play.api.data.Forms.{mapping, of, nonEmptyText}
import services.ProductService
import forms.{IDForm, CreateProductForm}
import play.api.data.format.Formats.{longFormat, doubleFormat}

/**
 * Controller for product specific operations.
 *
 * @author ob, scs
 */
object ProductController extends Controller {

  val id = "id"
  val name = "Name"
  val price = "Preis"
  val size = "Größe"
  val unit = "Einheit"
  val success = "success"


  /**
    * Form object for product data.
    */
  val productForm = Form(
    mapping(
      id -> of(longFormat),
      name -> nonEmptyText,
      price -> of(doubleFormat),
      size -> of(doubleFormat),
      unit -> nonEmptyText)(CreateProductForm.apply)(CreateProductForm.unapply))

  /**
    * Form object for retrieving product data from the database.
    */
  val selectProductForm = Form(
    mapping(
      id -> of(longFormat))(IDForm.apply)(IDForm.unapply))

  /**
    * Adds a new product with the given data to the database.
    *
    * @return page for a new product
    */
  def addProduct: Action[AnyContent] = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      productData => {
        val newProduct = services.ProductService.addProduct(productData.name, productData.price, productData.size, productData.unit)
        Redirect(routes.ProductController.newProductCreated(newProduct.id, newProduct.name, newProduct.price, newProduct.size, newProduct.unit)).
          flashing(success -> "Product saved!")
      })
  }

  /**
    * Changes an already existing product in the database.
    *
    * @return page for a changed product
    */
  def updateProduct: Action[AnyContent] = Action { implicit request =>
    productForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      productData => {
        val selectProduct = services.ProductService.updateProduct(productData.id, productData.name, productData.price, productData.size, productData.unit)
        Redirect(routes.ProductController.upgradeProduct(selectProduct.id, selectProduct.name, selectProduct.price, selectProduct.size, selectProduct.unit)).
          flashing(success -> "Product updated!")
      })
  }

  /**
    * Removes/deactivates a product.
    *
    * @return page for a deleted product
    */
  def rmProduct: Action[AnyContent] = Action { implicit request =>
    selectProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      deleteProductData => {
        val deleteProductVal = services.ProductService.rmProduct(deleteProductData.id)
        Redirect(routes.ProductController.productDeleted(deleteProductVal)).
          flashing(success -> "Product deleted!")
      })
  }

  /**
    * Retrieves a product from the database.
    *
    * @return page to change a product
    */
  def getProduct: Action[AnyContent] = Action { implicit request =>
    selectProductForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.badRequest())
      },
      selectProductData => {
        val selectProduct = services.ProductService.selectProduct(selectProductData.id)
        Redirect(routes.ProductController.changeProduct1(selectProduct.id, selectProduct.name, selectProduct.price, selectProduct.size, selectProduct.unit)).
          flashing(success -> "Product retrieved!")
      })
  }

  /**
    * Shows the view for a newly created product.
    */
  def newProductCreated(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.newProductCreated(id, name, price, size, unit))
  }

  /**
    * Shows the view to change a product.
    */
  def changeProduct1(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.changeProduct(id, name, price, size, unit, productForm))
  }

  /**
    * Shows the view for a changed product.
    */
  def upgradeProduct(id: Long, name: String, price: Double, size: Double, unit: String): Action[AnyContent] = Action {
    Ok(views.html.productUpdated(id, name, price, size, unit))
  }

  /**
    * Shows the view for a deleted product.
    */
  def productDeleted(deleted: Boolean): Action[AnyContent] = Action {
    Ok(views.html.productDeleted())
  }

  /**
    * List all products currently available in the database.
    */
  def showProducts: Action[AnyContent] = Action {
    Ok(views.html.allProducts(ProductService.availableProducts))
  }
}
