package dbaccess

import anorm.SQL
import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Pizza

/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait PizzaDaoT {

  /**
   * Creates the given user in the database.
   * @param pizza the user object to be stored.
   * @return the persisted user object
   */
  def addPizza(pizza: Pizza): Pizza = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Pizzas(name, price, ingredients, comment, supplements) values ({name}, {price}, {ingredients}, {comment}, {supplements})").on(
          'name -> pizza.name, 'price -> pizza.price, 'ingredients -> pizza.ingredients, 'comment -> pizza.comment, 'supplements -> pizza.supplements).executeInsert()
      pizza.id = id.get
    }
    pizza
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmPizza(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Pizzas where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def availablePizzas: List[Pizza] = {
    DB.withConnection { implicit c =>
      val selectPizzas = SQL("Select id, name, price, ingredients, comment, supplements from Pizzas;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val pizzas = selectPizzas().map(row => Pizza(row[Long]("id"), row[String]("name"), row[String]("price"),
        row[String]("ingredients"), row[String]("comment"), row[String]("supplements"))).toList
      pizzas
    }
  }
}

object PizzaDao extends PizzaDaoT
