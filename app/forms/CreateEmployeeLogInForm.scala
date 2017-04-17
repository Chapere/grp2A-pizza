package forms

/**
  * Form containing data to login an employee.
  * @param email email of the employee
  * @param password password of the employee
  */
case class CreateEmployeeLogInForm(email: String, password: String)
