package models

/**
 * User entity.
 * @param id database id of the user.
 * @param name name of the user.
 */
case class User(var id: Long, var name: String, var lastname: String, var adress: String, var city: String, var plz: String, distance: String, email: String, password: String, activeFlag: Int)
