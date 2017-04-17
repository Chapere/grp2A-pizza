package forms

/**
  * Form containing data to update data of an employee.
  * @param id ID of the employee
  * @param name name of the employee
  * @param lastname lastname of the employee
  * @param workplace workplace of the employee
  * @param acces access of the employee
  * @param accesLevel accessLevel of the employee
  * @param netRate netRate of the employee
  * @param email email of the employee
  * @param password password of the employee
  */
case class UpdateEmployeeForm(id: Long,  name: String, lastname: String, workplace: String,
                              acces: String, accesLevel: Int, netRate: Double, email: String,
                              password: String)
