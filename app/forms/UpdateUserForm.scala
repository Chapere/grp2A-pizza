package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class UpdateUserForm(name: String, lastname: String, adress: String, city: String, plz: String, distance: Double, email: String, password: String)
