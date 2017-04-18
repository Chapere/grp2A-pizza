package services

import dbaccess.{UserDao, UserDaoT}
import models.User

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait UserServiceT {

  val userDao: UserDaoT = UserDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addUser(name: String, lastname: String, adress: String, city: String, plz: String, distance: Double,
              email: String, password: String): User = {
    // create User
    val newUser = User(-1, name, lastname, adress, city, plz, distance, email, password, 1)
    // persist and return User
    userDao.addUser(newUser)
  }

  def chooseUser(id: Long): List[User] = {
    // create User
    // persist and return User
    UserDao.getUserByIdentification(id)
  }

  def selectUser(id: Long): User = {
    // create User
    // persist and return User
    UserDao.selectUserByIdentification(id)
  }

  def setUserFlag0(id: Long): Long = {
    // create User
    // persist and return User
    UserDao.deactivateUser(id)
  }

  def setUserFlag1(id: Long): Long = {
    // create User
    // persist and return User
    UserDao.activateUser(id)
  }

  def updateUser(id: Long, name: String, lastname: String, adress: String, city: String, plz: String, distance: Double,
                 email: String, password: String): User = {
    // update User
    val updateUserService = User(id, name, lastname, adress, city, plz, distance, email, password, 1)
    // persist and return User
    UserDao.updateUserDao(updateUserService)
  }

  def makeError(distance: Double, email: String, password: String): Double = {
    // create User
    val updateUserService = User(-1, null, null, null, null, null, distance, email, password, -1)
    // persist and return User
    UserDao.makeMistake(updateUserService)
  }

  def accesUserData(email: String, password: String): User = {
    // create User
    val logInUser = User(-1, null, null, null, null, null, 0, email, password, -1)
    // persist and return User
    userDao.getUser(logInUser)
  }

  def updateDistanceData(email: String, password: String, distance: Double): Double = {
    // create User
    val logInUser = User(-1, null, null, null, null, null, distance, email, password, -1)
    // persist and return User
    userDao.updateDistance(logInUser)
  }

  def getUserByID(id: Long): List[User] = {
    // create User
    // persist and return User
    userDao.getUserByIdentification(id)
  }

  /**
   * Removes a user by id from the system.
   * @param id users id.
   * @return a boolean success flag.
   */
  def rmUser(id: Long): Boolean = UserDao.rmUser(id)

  /**
   * Gets a list of all registered users.
   * @return list of users.
   */
  def registeredUsers: List[User] = {
    userDao.registeredUsers
  }
}

object UserService extends UserServiceT
