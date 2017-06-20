package services

import dbaccess.{PizzaDao, PizzaDaoT}
import models.Pizza

/**
  * Service class for pizza related operations.
  *
  * @author ob, scs
  */
trait PizzaServiceT {

  val pizzaDao: PizzaDaoT = PizzaDao

  /**
    * Adds a new pizza to the system.
    *
    * @param name        the name of a pizza
    * @param price       the price of a pizza
    * @param ingredients the ingredients of a pizza
    * @param comment     the comment for a pizza
    * @param supplements the supplements of a pizza
    * @return the new pizza
    */
  def addPizza(name: String, price: Double, ingredients: String,
               comment: String, supplements: String): Pizza = {
    // create User
    val newPizza = Pizza(-1, name, price, ingredients, comment, supplements)
    // persist and return User
    PizzaDao.addPizza(newPizza)
  }

  /**
    * Retrieve a pizza from the database.
    *
    * @param id the pizza's id
    * @return the pizza object
    */
  def selectPizza(id: Long): Pizza = {
    // create Pizza
    // persist and return Pizza
    PizzaDao.selectPizzaByIdentification(id)
  }

  /**
    * Changes the database entry of the given pizza.
    *
    * @param id          the id of a pizza
    * @param name        the name of a pizza
    * @param price       the price of a pizza
    * @param ingredients the ingredients of a pizza
    * @param comment     the comment for a pizza
    * @param supplements the supplements of a pizza
    * @return the changed pizza object
    */
  def updatePizza(id: Long, name: String, price: Double, ingredients: String,
                  comment: String, supplements: String): Pizza = {
    // create Pizza
    val updatePizzaService = Pizza(id, name, price, ingredients, comment, supplements)
    // persist and return Pizza
    PizzaDao.updatePizzaDao(updatePizzaService)
  }

  /**
    * Removes a pizza from the database.
    *
    * @param id the pizza's id
    * @return a boolean success flag
    */
  def rmPizza(id: Long): Boolean = PizzaDao.rmPizza(id)

  /**
    * Retrieves a list of available pizza from the database.
    *
    * @return a list of pizza objects
    */
  def availablePizza: List[Pizza] = {
    PizzaDao.availablePizzas
  }
}

object PizzaService extends PizzaServiceT
