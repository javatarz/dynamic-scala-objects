package me.karun.serialization.model

case class CaseClassWithNestedObjects(greeting: String, private val tokenNumber: Int, private val obj: CaseClassWithPrivateParameters) {
  def hello(name: String) = s"$greeting, $name. Your token number is $tokenNumber. ${obj.toString}"
}
