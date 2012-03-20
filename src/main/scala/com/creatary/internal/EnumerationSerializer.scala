/**
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.creatary.internal;
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * @author http://yuesong.posterous.com/making-enumerations-work-with-lift-json
 *
 */
class EnumerationSerializer(enums: Enumeration*) extends Serializer[Enumeration#Value] {

  val EnumerationClass = classOf[Enumeration#Value]

  val formats = Serialization.formats(NoTypeHints)

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Enumeration#Value] = {
    case (TypeInfo(EnumerationClass, _), json) => json match {
      case JString(value) => enums.find(_.values.exists(_.toString == value)).get.withName(value)
      case value          => throw new MappingException("Can't convert " + value + " to " + EnumerationClass)
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case i: Enumeration#Value => i.toString
  }
}
