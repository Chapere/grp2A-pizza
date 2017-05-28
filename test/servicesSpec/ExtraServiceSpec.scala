package servicesSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.ExtraService

/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class ExtraServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The ExtraService" should {

    "return a list of five extras" in memDB {
      ExtraService.availableExtras.length must be equalTo(5)
    }

    "return a list of six extras after adding an extra called mushrooms" in memDB {
      ExtraService.addExtra("mushrooms",1)
      ExtraService.availableExtras.length must be equalTo(6)
      ExtraService.availableExtras(5).name must be equalTo("mushrooms")
      ExtraService.availableExtras(5).price must be equalTo(1)
    }

    "return a list of seven extras after adding two extras called mushrooms" in memDB {
      ExtraService.addExtra("mushrooms",1)
      ExtraService.addExtra("mushrooms",1)
      ExtraService.availableExtras.length must be equalTo(7)
    }

    "return updated values for name and price of extras called mushrooms" in memDB {
      ExtraService.addExtra("mushrooms",1)
      ExtraService.addExtra("mushrooms",1)
      ExtraService.addExtra("mushrooms",1)
      ExtraService.updateExtra(5,"shitake",1)
      ExtraService.updateExtra(6,"mushrooms",0.5)
      ExtraService.updateExtra(7,"funghi",0.5)
      ExtraService.availableExtras.length must be equalTo(8)
      ExtraService.availableExtras(5).name must be equalTo("shitake")
      ExtraService.getExtraByID(5).head.price must be equalTo(1)
      ExtraService.selectExtra(6).name must be equalTo("mushrooms")
      ExtraService.getExtraByID(6).head.price must be equalTo(0.5)
      ExtraService.availableExtras(7).name must be equalTo("funghi")
      ExtraService.selectExtra(7).price must be equalTo(0.5)
      }

    "remove extra and return available extras" in memDB {
      ExtraService.addExtra("mushrooms",1)
      ExtraService.addExtra("mushrooms",1)
      ExtraService.availableExtras.length must be equalTo(7)
      ExtraService.rmExtra(5)
      ExtraService.selectExtraByID(6).name must be equalTo("mushrooms")
      ExtraService.selectExtraByID(6).price must be equalTo(1)
      ExtraService.availableExtras.length must be equalTo(6)
    }
  }

}
