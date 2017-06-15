package models

/**
  * Model for a user
  * @param id id
  * @param name name
  * @param lastname lastname
  * @param adress adress
  * @param city city
  * @param plz plz
  * @param distance distance
  * @param email email
  * @param password password
  * @param activeFlag activeFlag
  */
case class User(var id: Long, var name: String, var lastname: String,
                var adress: String, var city: String,
                var plz: String, distance: Double,
                email: String, password: String,
                activeFlag: Int)
