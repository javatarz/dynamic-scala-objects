package me.karun.model

case class CaseClassWithParameter(greeting: String) {
  def hello(name: String) = s"$greeting $name"
}
