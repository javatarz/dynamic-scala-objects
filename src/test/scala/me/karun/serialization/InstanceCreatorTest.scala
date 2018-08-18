package me.karun.serialization

import me.karun.serialization.InstanceCreator.richClassImplicit
import me.karun.serialization.model.{CaseClassWithParameter, CaseClassWithPrivateParameters, ClassWithoutParameters}
import org.scalatest.{Matchers, WordSpec}

class InstanceCreatorTest extends WordSpec with Matchers {

  "newInstance method" when {
    "provided a map" should {
      "create a new instance" in {
        val data: Map[String, Any] = Map("key" -> "Apples", "number" -> 1)
        val instance = classOf[CaseClassWithPrivateParameters].newInstance(data)

        instance shouldBe CaseClassWithPrivateParameters("Apples", 1)
      }
    }
  }

  "generateWithTestData method" when {
    "called without suppliers" should {
      "create an instance with default suppliers" in {
        val instance = classOf[ClassWithoutParameters].generateWithTestData()

        instance.hello("Test User 1") shouldBe "Hello, Test User 1."
      }
    }

    "called with overriding and default suppliers" should {
      val stringSupplier = () => "Bonjour"
      val suppliers: Map[Class[_], () => _] = Map(classOf[String] -> stringSupplier)

      "create an instance with suppliers" in {
        val instance = classOf[CaseClassWithParameter].generateWithTestData(suppliers)

        instance.hello("Test User 2") shouldBe "Bonjour, Test User 2. Your token number is 0. CaseClassWithPrivateParameters(Bonjour,0)"
      }

      "create an instance with data" in {
        val data: Map[String, Any] = Map("key" -> "Apples", "number" -> 1)
        val instance = classOf[CaseClassWithParameter].generateWithTestData(suppliers, data)

        instance.hello("Test User 2") shouldBe "Bonjour, Test User 2. Your token number is 0. CaseClassWithPrivateParameters(Apples,1)"
      }
    }
  }
}
