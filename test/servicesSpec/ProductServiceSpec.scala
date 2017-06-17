package servicesSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.Helpers.{running}
import play.api.test.FakeApplication
import services.ProductService
/**
  * @author Felix Thomas
  */
@RunWith(classOf[JUnitRunner])
class ProductServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The ProductService" should {

    "return a list of six products" in memDB {
      ProductService.availableProducts.length must be equalTo 6
    }

    "return a list of seven products after adding a product called Pommes" in memDB {
      ProductService.addProduct("Pommes",3,200,"g")
      ProductService.availableProducts.length must be equalTo 7
      ProductService.availableProducts(6).name must be equalTo "Pommes"
      ProductService.availableProducts(6).price must be equalTo 3
    }

    /*
    "return a list of seven extras after adding two extras called mushrooms" in memDB {
      ExtraService.addExtra("mushrooms",1)
      ExtraService.addExtra("mushrooms",1)
      ExtraService.availableExtras.length must be equalTo(7)
    }
    */

    "return updated values for name and price of product called Pommes" in memDB {
      ProductService.addProduct("Pommes",3,200,"g")
      ProductService.updateProduct(6,"Frites",4,250,"g")
      ProductService.availableProducts.length must be equalTo 7
      ProductService.selectProductByID(6).name must be equalTo "Frites"
      ProductService.selectProduct(6).price must be equalTo 4

    }

    "remove extra and return available extras" in memDB {
      ProductService.addProduct("Pommes",3,200,"g")
      ProductService.addProduct("Pommes",3,200,"g")
      ProductService.availableProducts.length must be equalTo 8
      ProductService.rmProduct(6)
      ProductService.getProductByID(7).head.name must be equalTo "Pommes"
      ProductService.selectProduct(7).price must be equalTo 3
      ProductService.availableProducts.length must be equalTo 7
    }
  }

}
