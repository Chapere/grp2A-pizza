package dbaccess

import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.User
import anorm.{ SQL, SqlParser }


/**
 * Data access object for user related operations.
 *
 * @author ob, scs, Kamil Gorszczyk
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
        SQL("insert into Users(name, lastname, adress, city, plz, email, password, activeFlag) values ({name}, {lastname}, {adress}, {city}, {plz}, {email}, {password}, 1)").on(
          'name -> user.name, 'lastname -> user.lastname, 'adress -> user.adress, 'city -> user.city, 'plz -> user.plz, 'email -> user.email, 'password -> user.password).executeInsert()
      user.id = id.get

      models.activeUser.name = user.name
      models.activeUser.lastname = user.lastname
      models.activeUser.adress = user.adress
      models.activeUser.city = user.city
      models.activeUser.plz = user.plz
      models.activeUser.email = user.email
      models.activeUser.activeFlag = 1
    }
    user
  }

  def updateUserDao(user: User): User = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("UPDATE Users SET name = {name}, lastname = {lastname}, adress = {adress}, city = {city}, plz = {plz}, email = {email}, password = {password} WHERE id = {id}").on(
          'name -> user.name, 'lastname -> user.lastname, 'adress -> user.adress, 'city -> user.city, 'plz -> user.plz, 'email -> user.email, 'password -> user.password, 'id -> models.Debug.updateUserId).executeInsert()
    }
    user
  }

  def displayUser(user: User): User = {

    DB.withConnection { implicit c =>
      val lastname: String =
        SQL("Select lastname from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("lastname").single)

      val adress: String =
        SQL("Select adress from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("adress").single)

      val city: String =
        SQL("Select city from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("city").single)


      val email: String =
        SQL("Select email from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("email").single)


      val plz: String =
        SQL("Select plz from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("plz").single)

      val name: String =
        SQL("Select name from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.str("name").single)

      val id: Long =
        SQL("Select id from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.long("id").single)

      val activeFlag: Int =
        SQL("Select activeFlag from Users where id = {id};").on(
          'id -> user.id).
          as(SqlParser.int("activeFlag").single)

      models.Debug.updateUserId = id

      val chooseUser = User(user.id, name, lastname, adress, city, plz, email, null, activeFlag)

      chooseUser

    }

  }

  def logInUser(user: User): String = {
    DB.withConnection { implicit c =>
      val lastname: String =
        SQL("Select lastname from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("lastname").single)
      models.activeUser.lastname = lastname

      val adress: String =
        SQL("Select adress from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("adress").single)
      models.activeUser.adress = adress

      val city: String =
        SQL("Select city from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("city").single)
      models.activeUser.city = city

      val plz: String =
        SQL("Select plz from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("plz").single)
      models.activeUser.plz = plz

      val email: String =
        SQL("Select email from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("email").single)
      models.activeUser.email = email

      val id: Int =
        SQL("Select id from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.int("id").single)
      models.activeUser.id = id

      val name: String =
        SQL("Select name from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.str("name").single)
      models.activeUser.name = name

      val activeFlag: Int =
        SQL("Select activeFlag from Users where email = {email} AND password = {password};").on(
          'email -> user.email, 'password -> user.password).
          as(SqlParser.int("activeFlag").single)
      models.activeUser.activeFlag = activeFlag

      models.activeUser.typ = "User"

      name
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
      val selectUsers = SQL("Select id, name, lastname, adress, city, plz, activeFlag from Users;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val users = selectUsers().map(row => User(row[Long]("id"), row[String]("name"), row[String]("lastname"), row[String]("adress"), row[String]("city"), row[String]("plz"), null, null, row[Int]("activeFlag"))).toList
      users
    }
  }

}

object UserDao extends UserDaoT
