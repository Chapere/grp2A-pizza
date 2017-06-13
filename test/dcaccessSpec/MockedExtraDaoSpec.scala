package dcaccessSpec

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

/**
  * Created by Felix on 12.06.2017.
  */
@RunWith(classOf[JUnitRunner])
class MockedExtraDaoSpec extends Specification with Mockito {

  "The ExtraService" should {
    "return a list of three extras" in {
      ExtraService.availableExtras.length must be equalTo(5)
    }
  }

  object ExtraService extends services.ExtraServiceT {
    override val extraDao = mock[dbaccess.ExtraDaoT]
    import models.Extra
    extraDao.registeredExtras returns List(Extra(0,"",0),
      Extra(1,"Champignions",0.80),
      Extra(2,"Paprika", 0.60),
      Extra(3,"Käse", 0.50),
      Extra(4,"Schinken", 0.90))
  }
}
