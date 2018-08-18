package me.karun.serialization

import java.util.function.Supplier

import me.karun.serialization.model.{CaseClassWithParameter, CaseClassWithPrivateParameters, ClassWithoutParameters}
import org.scalatest.{Matchers, WordSpec}
import InstanceCreator.richClassImplicit

class InstanceCreatorTest extends WordSpec with Matchers {

  "newInstance method" when {
    "provided a map" should {
      "create a new instance" in {
        val instance = classOf[CaseClassWithPrivateParameters].newInstance(Map("key" -> "value", "number" -> 1))

        instance shouldBe CaseClassWithPrivateParameters("value", 1)
      }
    }
  }

  "generateWithTestData method" when {
    "called without suppliers" should {
      "create an instance with default suppliers" in {
        val instance = classOf[ClassWithoutParameters].generateWithTestData()

        instance.hello("Test User 1") shouldBe "Hello Test User 1"
      }
    }

    "called with suppliers" ignore {
      val stringSupplier: Supplier[String] = () => ""
      val intSupplier: Supplier[Int] = () => 0
      val list = List(stringSupplier, intSupplier)

      "create an instance with suppliers" in {
        val instance = classOf[CaseClassWithParameter].generateWithTestData(list)

        instance.hello("Test User 2") shouldBe "Hello Test User 2"
      }
    }
  }
}
