package services

import dbaccess.{ExtraDao, ExtraDaoT}
import models.{Extra}

/**
 * Service class for extra related operations.
 *
 * @author ob, scs
 */
trait ExtraServiceT {

  val extraDao: ExtraDaoT = ExtraDao

  /**
   * Adds a new extra to the system.
   * @param name name of the new extra.
   * @return the new extra.
   */
  def addExtra(name: String, price: Double): Extra = {
    // create Extra
    val newExtra = Extra(-1, name, price)
    // persist and return Extra
    extraDao.addExtra(newExtra)
  }

  def selectExtra(id: Long): Extra = {
    // create Extra
    // persist and return Extra
    ExtraDao.selectExtraByIdentification(id)
  }


  def updateExtra(id: Long, name: String, price: Double): Extra = {
    // create Extra
    val updateExtraService = Extra(id, name, price)
    // persist and return Extra
    ExtraDao.updateExtraDao(updateExtraService)
  }


  def getExtraByID(id: Long): List[Extra] = {
    // create Extra
    // persist and return Extra
    extraDao.getExtraByIdentification(id)
  }

  def selectExtraByID(id: Long): Extra = {
    // create Extra
    // persist and return Extra
    extraDao.selectExtraByIdentification(id)
  }

  /**
   * Removes a extra by id from the system.
   * @param id extras id.
   * @return a boolean success flag.
   */
  def rmExtra(id: Long): Boolean = ExtraDao.rmExtra(id)

  /**
   * Gets a list of all registered extras.
   * @return list of extras.
   */
  def availableExtras: List[Extra] = {
    extraDao.registeredExtras
  }

}

object ExtraService extends ExtraServiceT
