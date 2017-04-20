package servicesSpec

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._
import services.UserService

@RunWith(classOf[JUnitRunner])
class UserServiceSpec extends Specification {

  def memDB[T](code: => T) =
    running(FakeApplication(additionalConfiguration = Map(
      "db.default.driver" -> "org.h2.Driver",
      "db.default.url" -> "jdbc:h2:mem:test;MODE=PostgreSQL"
    )))(code)

  "The UserService" should {

    "return a empty list of users first" in memDB {
      UserService.registeredUsers must beEmpty
    }

    "user Emil have to be registered at the start" in memDB {
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must be equalTo(1)
      registeredUsers(0).name must be equalTo("Emil")
    }

    "user Emil should have ID 1" in memDB {
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).id must be equalTo(1)
    }

    "Set user Emil inactive" in memDB {
      UserService.setUserFlag0(1)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).activeFlag must be equalTo(0)
    }

    "Set user Emil inactive and active again" in memDB {
      UserService.setUserFlag0(1)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).activeFlag must be equalTo(0)
      UserService.setUserFlag1(1)
      registeredUsers(1).activeFlag must be equalTo(1)
    }

    "add a user Taha" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234") must
        beTheSameAs("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
    }

    "return a list containing just Helge after adding user Helge" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must be equalTo(1)
      registeredUsers(0).name must be equalTo("Taha")
    }

    "return a list of two users after adding two users Helge" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.registeredUsers.length must be equalTo(2)
    }

    "add a user and update info" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234") must
        beTheSameAs("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.updateUser(1, "Thomas", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers(0).name must be equalTo("Thomas")
    }
  }
}
