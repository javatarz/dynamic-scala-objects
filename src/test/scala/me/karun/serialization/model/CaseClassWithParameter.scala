package me.karun.serialization.model

case class CaseClassWithParameter(greeting: String, private val number: Int) {
  def hello(name: String) = s"$greeting, $name. Your token number is $number."
}
