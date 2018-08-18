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

  def generateWithTestData(suppliers: Map[Class[_], () => _]): T = {
    val allSuppliers = defaultSuppliers ++ suppliers
    val objectCreator = () => {
      val constructor = clazz.getConstructors.head.asInstanceOf[Constructor[T]]
      val params: Array[_ <: Object] = constructor.getParameterTypes
        .map(paramType => paramType.generateWithTestData(allSuppliers))
        .array
        .asInstanceOf[Array[_ <: Object]]
      constructor.newInstance(params: _*)
    }
    val supplier = allSuppliers.getOrElse(clazz, objectCreator)
    supplier.apply().asInstanceOf[T]
  }

  def generateWithTestData(): T = generateWithTestData(defaultSuppliers)
}

object InstanceCreator {
  implicit def richClassImplicit[T](clazz: Class[T]): InstanceCreator[T] = InstanceCreator(clazz)
}