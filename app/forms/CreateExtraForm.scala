package forms

/**
  * Form containing data to create an extra.
  *
  * @param id    id of the extra
  * @param name  name of the extra
  * @param price price of the extra
  */
case class CreateExtraForm(id: Long, name: String, price: Double)
