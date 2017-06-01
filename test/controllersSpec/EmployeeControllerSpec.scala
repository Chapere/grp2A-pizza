package controllersSpec

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.test.FakeApplication
import play.api.test.Helpers.running

/**
  * Created by ifw15124 on 20.04.2017.
  */
@RunWith(classOf[JUnitRunner])
class EmployeeControllerSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

}
