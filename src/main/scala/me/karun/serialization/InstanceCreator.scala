package me.karun.serialization

import java.lang.reflect.Constructor

case class InstanceCreator[T](clazz: Class[T]) {

  import InstanceCreator.richClassImplicit

  private val defaultSuppliers: Map[Class[_], () => _] = Map(classOf[String] -> (() => ""), classOf[Int] -> (() => 0), classOf[Double] -> (() => 0.0))

  def newInstance(data: Map[String, Any]): T = clazz.getConstructors.head.asInstanceOf[Constructor[T]].newInstance(params(data = data): _*)

  def generateWithTestData(suppliers: Map[Class[_], () => _], data: Map[String, Any] = Map.empty): T = {
    val allSuppliers = defaultSuppliers ++ suppliers
    val objectCreator = () => clazz.getConstructors.head.asInstanceOf[Constructor[T]]
      .newInstance(params(allSuppliers, data): _*)

    allSuppliers.getOrElse(clazz, objectCreator).apply().asInstanceOf[T]
  }

  def generateWithTestData(): T = generateWithTestData(defaultSuppliers)

  private def params(suppliers: Map[Class[_], () => _] = Map.empty, data: Map[String, Any] = Map.empty) = {
    val privateFieldPrefix = s"${clazz.getName.replace(".", "$")}$$$$"

    clazz.getDeclaredFields
      .map(f => data.getOrElse(f.getName.replace(privateFieldPrefix, ""), f.getType.generateWithTestData(suppliers, data)))
      .array
      .asInstanceOf[Array[_ <: Object]]
  }
}

object InstanceCreator {
  implicit def richClassImplicit[T](clazz: Class[T]): InstanceCreator[T] = InstanceCreator(clazz)
}