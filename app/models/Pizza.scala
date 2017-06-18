package models

/**
  * Pizza entity.
  *
  * @param id          the id of a pizza
  * @param name        the name of a pizza
  * @param price       the price of a pizza
  * @param ingredients the ingredients of a pizza
  * @param comment     the comment for a pizza
  * @param supplements the supplements of a pizza
  */
case class Pizza(var id: Long, var name: String,
                 var price: Double, var ingredients: String,
                 var comment: String, var supplements: String)

