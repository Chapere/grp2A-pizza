package servicesSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.PizzaService

/**
  * Created by FT on 07.06.2017.
  */
@RunWith(classOf[JUnitRunner])
class PizzaServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The PizzaService" should {

    "return a list of five Pizza" in memDB {
      PizzaService.availablePizza.length must be equalTo (4)
    }

    "return a list of six Pizza after adding a Pizza called Popey" in memDB {
      PizzaService.addPizza("Popey",0.2,"Spinat","Stark","Alle")
      PizzaService.availablePizza.length must be equalTo (5)
      PizzaService.availablePizza(4).name must be equalTo ("Popey")
      PizzaService.availablePizza(4).comment must be equalTo ("Stark")
    }

    "return updated values for name and comment of Pizza called Popey" in memDB {
      PizzaService.addPizza("Popey",0.2,"Spinat","Stark","Alle")
      PizzaService.updatePizza(4, "Popey", 0.3,"Spinat","Heiß","Nix")
      PizzaService.selectPizza(4).name must be equalTo ("Popey")
      PizzaService.selectPizza(4).comment must be equalTo ("Heiß")
    }

    "remove Pizza and return available Pizza" in memDB {
      PizzaService.addPizza("Popey",0.2,"Spinat","Stark","Alle")
      PizzaService.addPizza("Popey",0.2,"Spinat","Stark","Alle")
      PizzaService.availablePizza.length must be equalTo (6)
      PizzaService.rmPizza(4)
      PizzaService.availablePizza(4).name must be equalTo ("Popey")
      PizzaService.availablePizza(4).comment must be equalTo ("Stark")
      PizzaService.availablePizza.length must be equalTo (5)
    }
  }
}
