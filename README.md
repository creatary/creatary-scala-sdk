# creatary-scala-sdk

A Scaka SDK that provides a simple API to connect your web application with the Creatary platform.

send SMS, query location, charge subscribers and checking your transaction all through their mobile operator, in a revenue share, no upfront investment model._

http://creatary.com

# How to use

You can build project using sbt version > 0.10

then its easy to use:

```scala
val creatary = new Creatary("telcoassetmarketplace.com")

//sending sms
val sms = Sms("Hello world")

creatary.smser.send(sms, "valid_access_token") match {
  case Response(Status("0", _)) => println("ok")
  case _ => println("something wrong")
}

//fetching location
val location =
  creatary.localizer.retrieveLocation("valid_access_token") match {
    case LocationResponse(Status("0", _), loc) => loc
    case _ => None
  }

println(location)

//requesting charging
val chargeReq = ChargeRequest(CODE, charging_code = "10")

creatary.charger.charge(chargeReq, "valid_access_token") match {
  case Response(Status("0", _)) => println("ok")
  case _ => println("something wrong")
}
```
