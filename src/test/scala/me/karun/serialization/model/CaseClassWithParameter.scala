package me.karun.serialization.model

case class CaseClassWithParameter(greeting: String) {
  def hello(name: String) = s"$greeting $name"
}
