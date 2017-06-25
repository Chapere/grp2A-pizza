package services

import dbaccess.{ExtraDao, ExtraDaoT}
import models.Extra

/**
  * Service class for extra related operations.
  *
  * @author ob, scs
  */
trait ExtraServiceT {

  val extraDao: ExtraDaoT = ExtraDao

  /**
    * Adds a new extra to the system.
    *
    * @param name  the name of an extra
    * @param price the price of an extra
    * @return the new extra
    */
  def addExtra(name: String, price: Double): Extra = {
    // create Extra
    val newExtra = Extra(-1, name, price)
    // persist and return Extra
    extraDao.addExtra(newExtra)
  }

  /**
    * Retrieves an extra from the database.
    *
    * @param id the extra's id
    * @return the extra object
    */
  def selectExtra(id: Long): Option[Extra] = {
    // create Extra
    // persist and return Extra
    ExtraDao.selectExtraByIdentification(id)
  }


  /**
    * Changes the database entry of the given extra.
    *
    * @param id    the extra's id
    * @param name  the name of an extra
    * @param price the price of an extra
    * @return the changed extra object
    */
  def updateExtra(id: Long, name: String, price: Double): Extra = {
    // create Extra
    val updateExtraService = Extra(id, name, price)
    // persist and return Extra
    ExtraDao.updateExtraDao(updateExtraService)
  }

  /**
    * Retrieves an extra from the database.
    *
    * @param id the extra's id
    * @return the extra object
    */
  def getExtraByID(id: Long): List[Extra] = {
    // create Extra
    // persist and return Extra
    extraDao.getExtraByIdentification(id)
  }

  /**
    * Retrieves an extra from the database.
    *
    * @param id the extra's id
    * @return the extra object
    */
  def selectExtraByID(id: Long): Option[Extra] = {
    // create Extra
    // persist and return Extra
    extraDao.selectExtraByIdentification(id)
  }

  /**
    * Removes an extra from the database.
    *
    * @param id the extra's id
    * @return a boolean success flag
    */
  def rmExtra(id: Long): Boolean = ExtraDao.rmExtra(id)

  /**
    * Retrieves a list of available extras from the database.
    *
    * @return a list of extra objects
    */
  def availableExtras: List[Extra] = {
    extraDao.registeredExtras
  }

}

object ExtraService extends ExtraServiceT
