package forms

/**
 * Form containing data to create a user.
 * @param name name of the user.
 */
case class CreateUserLogInForm(email: String, password: String)
