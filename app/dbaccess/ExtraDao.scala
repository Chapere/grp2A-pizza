package dbaccess

import play.api.Play.current
import play.api.db.DB
import anorm.NamedParameter.symbol
import models.Extra
import anorm.{SQL, SqlParser}


/**
  * Data access object for extra related operations.
  *
  * @author ob, scs, Kamil Gorszczyk
  */
trait ExtraDaoT {
  val id1 = "id"
  val name = "name"
  val price = "price"
  val selectExtrasMain = "SELECT * FROM Extras WHERE id = {id};"

  /**
    * Creates the given extra in the database.
    *
    * @param extra the extra object to be stored.
    * @return the persisted extra object
    */
  def addExtra(extra: Extra): Extra = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("insert into Extras(name, price) values ({name}, {price})").on(
          'name -> extra.name, 'price -> extra.price).executeInsert()
    }
    extra
  }

  def updateExtraDao(extra: Extra): Extra = {
    DB.withConnection { implicit c =>
      val id: Option[Long] =
        SQL("UPDATE Extras SET name = {name}, price = {price} WHERE id = {id}").on(
          'name -> extra.name, 'price -> extra.price, 'id -> extra.id).executeInsert()
    }
    extra
  }

  def getExtraByIdentification(id: Long): List[Extra] = {
    DB.withConnection { implicit c =>
      val selectExtras = SQL(selectExtrasMain).on(
        'id -> id)
      val extras = selectExtras().map(row => Extra(row[Long](id1),
        row[String](name), row[Double](price))).toList
      extras
    }
  }

  def selectExtraByIdentification(id: Long): Extra = {

    DB.withConnection { implicit c =>
      val selectExtra = SQL(selectExtrasMain).on(
        'id -> id)
      val extras = selectExtra().map(row => Extra(row[Long](id1),
        row[String](name), row[Double](price))).toList
      extras.head
    }
  }

  /**
    * Removes a extra by id from the database.
    *
    * @param id the extras id
    * @return a boolean success flag
    */

  def rmExtra(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      val rowsCount = SQL("delete from Extras where id = ({id})").on('id -> id).executeUpdate()
      rowsCount > 0
    }
  }

  /**
    * Returns a list of available extra from the database.
    *
    * @return a list of extra objects.
    */
  def registeredExtras: List[Extra] = {
    DB.withConnection { implicit c =>
      val selectExtras = SQL("Select * from Extras;")
      // Transform the resulting Stream[Row] to a List[(String,String)]
      val extras = selectExtras().map(row => Extra(row[Long](id1),
        row[String](name), row[Double](price))).toList
      extras
    }
  }

}

object ExtraDao extends ExtraDaoT
