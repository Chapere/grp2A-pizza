package forms

/**
  * Form containing data to update a user.
  * @param id updated ID of the user
  * @param name updated name of the user
  * @param lastname updated lastname of the user
  * @param adress updated address of the user
  * @param city updated city of the user
  * @param plz updated plz of the user
  * @param email updated email of the user
  * @param password updated password of the user
  */
case class UpdateUserForm(id: Long, name: String, lastname: String, adress: String, city: String,
                          plz: String, email: String, password: String)
