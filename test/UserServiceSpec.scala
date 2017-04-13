import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
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

    /**"add a user Taha" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstraße 14", "München", "84587", 0 , "taha@gmx", "123") must
        be equalTo("Taha", "Obed", "Flurstraße 14", "München", "84587", 0 , "taha@gmx", "123")
    }

    "add a user Taha" in memDB {
      UserService.addUser("Taha", "Obed", "Flurstraße 14", "München", "80123", 0, "taha@gmx", "123").
      must be equalTo("Taha")
      UserService.addUser("Obed").lastname must be equalTo("Obed")
      UserService.addUser("Flurstraße 14").adress must be equalTo("Flurstraße 14")
      UserService.addUser("München").city must be equalTo("München")
      UserService.addUser("80142").plz must be equalTo("80142")
      UserService.addUser(0).distance must be equalTo(0)
      UserService.addUser("taha@gmx").email must be equalTo("taha@gmx")
      UserService.addUser("123").password must be equalTo("123")
    }

    "add a user Helga" in memDB {
      UserService.addUser("Helga").name must be equalTo("Helga")
    }

    "return a list containing just Helge after adding user Helge" in memDB {
      UserService.addUser("Helge")
      val registeredUsers = UserService.registeredUsers
      registeredUsers.length must be equalTo(1)
      registeredUsers(0).name must be equalTo("Helge")
    }

    "return a list of two users after adding two users Helge" in memDB {
      UserService.addUser("Helge")
      UserService.addUser("Helge")
      UserService.registeredUsers.length must be equalTo(2)
    } */
  }
}
