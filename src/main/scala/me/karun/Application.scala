package me.karun

import me.karun.model.{CaseClassWithParameter, CaseClassWithPrivateParameters, ClassWithoutParameters}
import InstanceCreator.richClassImplicit

object Application extends App {
  //  val stringSupplier: Supplier[String] = () => ""
  //  val intSupplier: Supplier[Int] = () => 0
  //  private val list = List(stringSupplier, intSupplier)

  println(classOf[ClassWithoutParameters].generateWithTestData().hello("Test User 1"))
  println(classOf[CaseClassWithParameter].generateWithTestData().hello("Test User 2"))
  println(classOf[CaseClassWithPrivateParameters].newInstance(Map("key" -> "value", "number" -> 1)))
}
