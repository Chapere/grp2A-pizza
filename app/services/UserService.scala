package services

import dbaccess.{UserDao, UserDaoT}
import models.User

/**
 * Service class for user related operations.
 *
 * @author ob, scs
 */
trait UserServiceT {

  val error = "error"

  val userDao: UserDaoT = UserDao

  /**
   * Adds a new user to the system.
   * @param name name of the new user.
   * @return the new user.
   */
  def addUser(name: String, lastname: String, adress: String,
              city: String, plz: String, distance: Double,
              email: String, password: String): User = {
    // create User
    val newUser = User(-1, name, lastname, adress, city, plz, distance, email, password, 1)
    // persist and return User
    userDao.addUser(newUser)
  }

  /**
    * choose an user out of the database
    * @param id the id of the user
    * @return an user
    */
  def chooseUser(id: Long): List[User] = {
    // create User
    // persist and return User
    UserDao.getUserByIdentification(id)
  }

  /**
    * select an user out of the database
    * @param id the if of the user
    * @return an user
    */
  def selectUser(id: Long): User = {
    // create User
    // persist and return User
    UserDao.selectUserByIdentification(id)
  }

  /**
    * set the user flag of an user to zero (Deactivated)
    * @param id the id of the user
    * @return an user with flag zero
    */
  def setUserFlag0(id: Long): Long = {
    // create User
    // persist and return User
    UserDao.deactivateUser(id)
  }

  /**
    * set the user flag of an user to one (Activated)
    * @param id the id of the user
    * @return an user with flag one
    */
  def setUserFlag1(id: Long): Long = {
    // create User
    // persist and return User
    UserDao.activateUser(id)
  }

  /**
    * update user information
    * @param id the id of the user
    * @param name name
    * @param lastname lastname
    * @param adress adress
    * @param city city
    * @param plz plz
    * @param distance distance
    * @param email email
    * @param password passwort
    * @return an updated user
    */
  def updateUser(id: Long, name: String, lastname: String, adress: String,
                 city: String, plz: String, distance: Double,
                 email: String, password: String): User = {
    // update User
    val updateUserService = User(id, name, lastname, adress,
      city, plz, distance, email, password, 1)
    // persist and return User
    UserDao.updateUserDao(updateUserService)
  }

  /**
    * make an error when user informations occur
    * @param distance distance error
    * @param email email
    * @param password password
    * @return
    */
  def makeError(distance: Double, email: String, password: String): Double = {
    // create User
    val updateUserService = User(-1, error, error, error,
      error, error, distance, email, password, -1)
    // persist and return User
    UserDao.makeMistake(updateUserService)
  }

  /**
    * access with userinformation
    * @param email email
    * @param password password
    * @return user information
    */
  def accesUserData(email: String, password: String): User = {
    // create User
    val logInUser = User(-1, error, error, error, error, error, 0, email, password, -1)
    // persist and return User
    userDao.getUser(logInUser)
  }

  /**
    * update the distance of an user
    * @param email email
    * @param password password
    * @param distance the updated distance
    * @return the updated user
    */
  def updateDistanceData(email: String, password: String, distance: Double): Double = {
    // create User
    val logInUser = User(-1, error, error, error, error, error,  distance, email, password, -1)
    // persist and return User
    userDao.updateDistance(logInUser)
  }

  /**
    * select an user with his id
    * @param id the specific user
    * @return the choosen user
    */
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