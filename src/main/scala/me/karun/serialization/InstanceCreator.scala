package me.karun.serialization

import java.lang.reflect.Constructor
import java.util.function.Supplier

case class InstanceCreator[T](clazz: Class[T]) {
  def newInstance(values: Map[String, Any]): T = {
    val params = clazz.getDeclaredFields
      .map(_.getName)
      .map(values(_))
      .array
      .asInstanceOf[Array[_ <: Object]]

    clazz.getConstructors.head.asInstanceOf[Constructor[T]].newInstance(params: _*)
  }

  def generateWithTestData(suppliers: List[Supplier[_]]): T = {
    generateWithTestData()
  }

  def generateWithTestData(): T = {
    import InstanceCreator.richClassImplicit

    clazz match {
      case c if c == classOf[String] => "".asInstanceOf[T]
      case c if c == classOf[Int] => 0.asInstanceOf[T]
      case c if c == classOf[Double] => 0.5.asInstanceOf[T]
      case c =>
        val constructor = c.getConstructors.head.asInstanceOf[Constructor[T]]
        val params: Array[_ <: Object] = constructor.getParameterTypes
          .map(paramType => paramType.generateWithTestData())
          .array
          .asInstanceOf[Array[_ <: Object]]
        constructor.newInstance(params: _*)
    }
  }
}

object InstanceCreator {
  implicit def richClassImplicit[T](clazz: Class[T]): InstanceCreator[T] = InstanceCreator(clazz)
}