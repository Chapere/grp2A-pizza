package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreateEmployeeForm(name: String, lastname: String, workplace: String, acces: String, accesLevel: Int, netRate: Double, email: String, password: String)
