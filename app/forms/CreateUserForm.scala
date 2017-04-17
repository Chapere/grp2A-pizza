package forms

/**
  * Form containing data to create a user.
  * @param name name of the user
  * @param lastname lastname of the user
  * @param adress address of the user
  * @param city city of the user
  * @param plz plz of the user
  * @param email email of the user
  * @param password password of the user
  */
case class CreateUserForm(name: String, lastname: String, adress: String, city: String, plz: String,
                          email: String, password: String)
