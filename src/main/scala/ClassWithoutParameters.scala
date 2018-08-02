import java.lang.reflect.Constructor

class ClassWithoutParameters {
  def hello(name: String) = s"Hello $name"
}

case class CaseClassWithParameter(greeting: String) {
  def hello(name: String) = s"$greeting $name"
}

object Application extends App {
  println(create(classOf[ClassWithoutParameters]).hello("Test User 1"))
  println(create(classOf[CaseClassWithParameter]).hello("Test User 2"))

  private def create[T](clazz: Class[T]): T = {
    clazz match {
      case c if c == classOf[String] => "".asInstanceOf[T]
      case c if c == classOf[Int] => 0.asInstanceOf[T]
      case c if c == classOf[Double] => 0.5.asInstanceOf[T]
      case c if c == classOf[Double] => 0.5.asInstanceOf[T]
      case c =>
        val constructor = c.getConstructors.head.asInstanceOf[Constructor[T]]
        val params: Array[_ <: Object] = constructor.getParameterTypes
          .map(paramType => create(paramType))
          .array
          .asInstanceOf[Array[_ <: Object]]
        constructor.newInstance(params: _*)
    }
  }
}

class DataCreator