# creatary-scala-sdk

A Scala SDK that provides a simple API to connect your web application with the Creatary platform.

send SMS, query location, charge subscribers and checking your transaction all through their mobile operator, in a revenue share, no upfront investment model.

http://creatary.com

# How to use

You can build project using sbt version > 0.10

then its easy to use:

```scala
val creatary = new Creatary("telcoassetmarketplace.com")
val access_token = "valid_access_token"

//sending sms
try {
  val sms = Sms("Hello world")
  creatary.send(sms, access_token)
} catch {
  case e: CreataryException => println("something wrong")
}

//fetching location
val location = try {
  creatary.findLocation(access_token)
} catch {
  case e: CreataryException => None
}

println(location)

//requesting charging
try {
  val chargeReq = ChargeRequest(CODE, charging_code = "10")
  creatary.charge(chargeReq, access_token)
} catch {
  case e: CreataryException => println("something wrong")
}
```

and a bit more verbose version in java

```java
Creatary creatary = new Creatary("telcoassetmarketplace.com");
String access_token = "valid_access_token";
// sending sms
try {
  Sms sms = new Sms("Hello world", null, null);
  creatary.send(sms, access_token);
} catch (CreataryException e) {
  System.out.println("something wrong");
}

// fetching location
LocationResponse location = null;
try {
  location = creatary.findLocation(access_token);
} catch (CreataryException e) {
  System.out.println("something wrong");
}

System.out.println(location);

// requesting charging
try {
  ChargeRequest chargeReq = new ChargeRequest("CODE", null, "10");
  creatary.charge(chargeReq, access_token);
} catch (CreataryException e) {
  System.out.println("something wrong");
}
```

Using maven just add our repository and dependency
```xml
<project>
<dependencies>
	<dependency>
		<groupId>com.creatary</groupId>
		<artifactId>creatary-scala-sdk_2.9.1</artifactId>
		<version>1.0</version>
	</dependency>
</dependencies>

<repositories>
	<repository>
		<id>creatary</id>
		<url>http://creatary.github.com/</url>
	</repository>
</repositories>
</project>
```

		