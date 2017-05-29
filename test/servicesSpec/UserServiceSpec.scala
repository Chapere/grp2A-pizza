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

    "set new user Taha inactive and active again" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").activeFlag must
        beEqualTo(1)
      UserService.setUserFlag0(2)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).activeFlag must beEqualTo(0)
      UserService.setUserFlag1(2)
      val registeredUsers2 = UserService.registeredUsers
      registeredUsers2(1).activeFlag must beEqualTo(1)
    }

    "add a user Taha and update the information" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").name must
        beEqualTo("Taha")
      UserService.updateUser(2, "Thomas", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").name must
        beEqualTo("Thomas")
    }

    "add a user Taha" in memDB {
      val registeredUsers = UserService.registeredUsers
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").name must
        beEqualTo("Taha")
    }

    "add a user and update info" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.updateUser(2, "Thomas", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).name must be equalTo("Thomas")
    }

    "add several users and choose them by their identifaction number" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234")
      UserService.chooseUser(2).head.name must beEqualTo("Taha")
      UserService.addUser("Tim", "Misu", "Vliestr 4", "München", "84792", 0, "tim@pronto", "1234")
      UserService.selectUser(3).name must beEqualTo("Tim")
      UserService.addUser("Thomas", "Krein", "Maßstr 56", "München", "84125", 0, "thomas@pronto", "1234")
      UserService.getUserByID(4).head.name must beEqualTo("Thomas")
    }

    "remove an user from the registered users list" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").name must
        beEqualTo("Taha")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must beEqualTo(2)
      UserService.rmUser(2)
      val registeredUsers2 = UserService.registeredUsers
      registeredUsers2.length must beEqualTo(1)
    }

    "If user writes wrong distance, make an error and delete the account" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 10000, "taha@pronto", "1234")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must beEqualTo(2)
      UserService.makeError(10000, "taha@pronto", "1234")
      val registeredUsers2 = UserService.registeredUsers
      registeredUsers2.length must beEqualTo(1)
    }

    "update the distance of an user" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstr 14", "München", "83723", 0, "taha@pronto", "1234").distance must
        beEqualTo(0)
      UserService.updateDistanceData("taha@pronto", "1234", 200)
      val registeredUsers = UserService.registeredUsers
      registeredUsers(1).distance must beEqualTo(200)
    }

    "create user with loginData to access the website" in memDB {
      UserService.accesUserData("user", "user").name must beEqualTo("Emil")
    }
  }
}
