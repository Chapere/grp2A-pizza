package dcaccessSpec

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import models.Extra

/**
  * @author Felix Thomas
  */

@RunWith(classOf[JUnitRunner])
class MockedExtraDaoSpec extends Specification with Mockito

  /*
  "The ExtraService" should {

    "return a list of five extras" in {
      ExtraService.availableExtras.length must be equalTo(5)
    }

    "return a newly added extra called mushrooms" in {
      ExtraService.addExtra("mushrooms", 1).price must be equalTo(1)
      ExtraService.getExtraByID(5).head.name must be equalTo("mushrooms")
      //ExtraService.availableExtras(5).name must be equalTo("mushrooms")
      //ExtraService.availableExtras(5).price must be equalTo(1)
    }

  }

  object ExtraService extends services.ExtraServiceT {
    override val extraDao = mock[dbaccess.ExtraDaoT]
    extraDao.registeredExtras returns List(Extra(0,"",0),
      Extra(1,"Champignions",0.80),
      Extra(2,"Paprika", 0.60),
      Extra(3,"Käse", 0.50),
      Extra(4,"Schinken", 0.90))

    extraDao.addExtra(null) returns Extra(5,"mushrooms",1)
    extraDao.getExtraByIdentification(5) returns List(Extra(5,"mushrooms",1))
  }
  */

