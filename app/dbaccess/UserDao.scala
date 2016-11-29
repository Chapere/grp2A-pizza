package dbaccess

import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.User
import anorm.{ SQL, SqlParser }


/**
 * Data access object for user related operations.
 *
 * @author ob, scs
 */
trait UserDaoT {

  /**
   * Creates the given user in the database.
   * @param user the user object to be stored.
   * @return the persisted user object
   */
  def addUser(user: User): User = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Users(name, lastname, adress, city, plz, email, password) values ({name}, {lastname}, {adress}, {city}, {plz}, {email}, {password})").on(
          'name -> user.name, 'lastname -> user.lastname, 'adress -> user.adress, 'city -> user.city, 'plz -> user.plz, 'email -> user.email, 'password -> user.password).executeInsert()
      user.id = id.get
    }
    user
  }

  def logInUser(user: User): String = {
    DB.withConnection { implicit c =>
      val lang: String =
          SQL("Select name from Users where email = {email} AND password = {password};").on(
            'email -> user.email, 'password -> user.password).
            as(SqlParser.str("name").single)
      lang
    }
  }

  /**
   * Removes a user by id from the database.
   * @param id the users id
   * @return a boolean success flag
   */
  def rmUser(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Users where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
   * Returns a list of available user from the database.
   * @return a list of user objects.
   */
  def registeredUsers: List[User] = {
      DB.withConnection { implicit c =>
      val selectUsers = SQL("Select id, name, lastname, adress, city, plz from Users;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val users = selectUsers().map(row => User(row[Long]("id"), row[String]("name"), row[String]("lastname"), row[String]("adress"), row[String]("city"), row[String]("plz"), null, null)).toList
      users
    }
  }

}

object UserDao extends UserDaoT
