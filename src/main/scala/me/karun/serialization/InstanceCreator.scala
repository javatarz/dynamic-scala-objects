package me.karun.serialization

import java.lang.reflect.Constructor

case class InstanceCreator[T](clazz: Class[T]) {
  def newInstance(values: Map[String, Any]): T = {
    val privateFieldPrefix = s"${clazz.getName.replace(".", "$")}$$$$"
    val params = clazz.getDeclaredFields
      .map(_.getName.replace(privateFieldPrefix, ""))
      .map(values(_))
      .array
      .asInstanceOf[Array[_ <: Object]]

    clazz.getConstructors.head.asInstanceOf[Constructor[T]].newInstance(params: _*)
  }

  private val defaultSuppliers: Map[Class[_], () => _] = Map(classOf[String] -> (() => ""), classOf[Int] -> (() => 0), classOf[Double] -> (() => 0.0))

  import InstanceCreator.richClassImplicit

  def generateWithTestData(suppliers: Map[Class[_], () => _], data: Map[String, Any] = Map.empty): T = {
    val allSuppliers = defaultSuppliers ++ suppliers
    val objectCreator = () => {
      val privateFieldPrefix = s"${clazz.getName.replace(".", "$")}$$$$"
      val params = clazz.getDeclaredFields
        .map(f => data.getOrElse(f.getName.replace(privateFieldPrefix, ""), f.getType.generateWithTestData(allSuppliers, data)))
        .array
        .asInstanceOf[Array[_ <: Object]]

      clazz.getConstructors.head.asInstanceOf[Constructor[T]].newInstance(params: _*)
    }
    val supplier = allSuppliers.getOrElse(clazz, objectCreator)
    supplier.apply().asInstanceOf[T]
  }

  def generateWithTestData(): T = generateWithTestData(defaultSuppliers)
}

object InstanceCreator {
  implicit def richClassImplicit[T](clazz: Class[T]): InstanceCreator[T] = InstanceCreator(clazz)
}