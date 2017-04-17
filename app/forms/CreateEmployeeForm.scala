package forms

/**
  * Form containing data to create a employee.
  * @param name name of the employee
  * @param lastname lastname of the employee
  * @param workplace workplace of the employee
  * @param acces access of the employee
  * @param accesLevel accessLevel of the employee
  * @param netRate netRate of the employee
  * @param email email of the employee
  * @param password password of the employee
  */
case class CreateEmployeeForm(name: String, lastname: String, workplace: String, acces: String,
                             accesLevel: Int, netRate: Double, email: String, password: String)
