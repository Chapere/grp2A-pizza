package services

import dbaccess.{PizzaDao, PizzaDaoT}
import models.Pizza

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait PizzaServiceT {

  val pizzaDao: PizzaDaoT = PizzaDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addPizza(name: String, price: Double, ingredients: String, comment: String, supplements: String): Pizza = {
    // create User
    val newPizza = Pizza(-1, name, price, ingredients, comment, supplements)
    // persist and return User
    PizzaDao.addPizza(newPizza)
  }

  def selectPizza(id: Long): Pizza = {
    // create Pizza
    // persist and return Pizza
    PizzaDao.selectPizzaByIdentification(id)
  }


  def updatePizza(id: Long, name: String, price: Double, ingredients: String, comment: String, supplements: String): Pizza = {
    // create Pizza
    val updatePizzaService = Pizza(id, name, price, ingredients, comment, supplements)
    // persist and return Pizza
    PizzaDao.updatePizzaDao(updatePizzaService)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmPizza(id: Long): Boolean = PizzaDao.rmPizza(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def availablePizza: List[Pizza] = {
    PizzaDao.availablePizzas
  }

}

object PizzaService extends PizzaServiceT
