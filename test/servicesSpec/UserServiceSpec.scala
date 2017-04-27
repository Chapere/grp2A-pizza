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

    "user Emil have to be registered at the start" in memDB {
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must be equalTo(1)
      registeredUsers(0).name must be equalTo("Emil")
    }

    "user Emil should have ID 1" in memDB {
      val registeredUsers = UserService.registeredUsers
      registeredUsers(0).id must be equalTo(1)
    }

    "Set user Emil inactive" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.setUserFlag0(1)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).activeFlag must be equalTo(0)
    }

    "Set user Emil inactive and active again" in memDB {
      UserService.setUserFlag0(0)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(0).activeFlag must be equalTo(0)
      UserService.setUserFlag1(1)
      registeredUsers(0).activeFlag must be equalTo(1)
    }

    "add a user Taha" in memDB {
      val registeredUsers = UserService.registeredUsers
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234") must
        beEqualTo(registeredUsers(1))
    }

    "add a user Taha and remove him" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must beEqualTo(2)
      UserService.rmUser(2)
      registeredUsers.length must beEqualTo(1)
    }

    "return a list containing just Emil & Taha after adding user Taha" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must be equalTo(2)
      registeredUsers(0).name must be equalTo("Emil")
      registeredUsers(1).name must be equalTo("Taha")
    }

    "return a list of three users after adding two users Taha" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.registeredUsers.length must be equalTo(3)
    }

    "add a user and update info" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.updateUser(2, "Thomas", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).name must be equalTo("Thomas")
    }
  }
}
