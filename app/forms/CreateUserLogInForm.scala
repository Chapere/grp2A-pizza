package forms

/**
 * Form containing data to login a user.
 * @param email email of the user.
 * @param password password of the user
 */
case class CreateUserLogInForm(email: String, password: String)
