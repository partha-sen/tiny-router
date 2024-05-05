
# tiny-router

## What is “tiny-router” ?

It’s a maven library, can be added to the Kafka producer and consumer component. By defining maven dependency as follows.
``` xml
<dependency>
<groupId>io.github.partha-sen</groupId>
<artifactId>tiny-router</artifactId>
<version>1.0.0</version>
</dependency>
```

It’s helps to dispatch various type of Kafka message to appropriate message handler.

## What is the need of it?
### Little Background 

1. #### Sync microservice state change to monolith.

One obvious challenge of application transformation from monolith to microservice with incremental migration release is synchronizing the systems together.
if you enforce all write operation only with newly build microservice, but still read some read is happening from monolith, then you should publish state change events to asynchronous system and farther process it to update state of monolith application.
The advantage of doing in asynchronously is to optimize performance and loose coupling of monolith API call. When system gets mature and completely replaced monolith, we just stop processing events. We don’t have to remove any dead code related to Legacy API call in our microservice.

2. #### How many “topic” you should create to update legacy application?

It depends on the system design.  We can group all logically related operation and then for each group define a topic. In our case we have defined very few Kafka topic, each topic handle multiple type of massages.
Example: All user profile related operation we defined only one topic, and it handle operation like “update address” and “update contact” etc. and payload for address and contact are also different.

## Challenge with multiple type of message processing in single Kafka consumer are.
1.	Define message format to accommodate all type of messages and operation.
2.	Handle message serialization and deserialization.
3.	Message conversion to the java target Type.
4.	Delegate message to the appropriate handler to process the message.

## How “tiny-router” works?
“tiny-router” first prepare routeMap by processing @RouteEntry annotation, which is defined in message Handler Class.  


#### Code snippet of routeMap and RouteData
``` java
private final Map<String, RouteData<T>>  routeMap = new HashMap<>();

public class RouteData<T> {
    T instance;
    Method method;
    Class<?> parameterType;
}
```

All valid message handle must have public method with single argument and method should annotated with 
``` java
@RouteEntry(action="example_action")  
```

### “tiny-router” also do

 Define message format to accommodate all type of messages and operation.
 1. #### Message format to accommodate all type of messages and operation.
      How to generically represent message structure to support multiple operation and payload.
      “tiny-router” and defined message structure as
``` java 
      public class MessageEnvelop<P> {
      String action;
      String version;
      P payload;
      }
```
Action field represent Operation.<br>
Example: “Register Product” or “Place Order” or “Update Address” etc.<br>
Version field represent payload version. This Is optional filed.<br>
Example: “v1” or “v2” etc.<br>
Payload field represent message data, It is any Java DTO object.<br>
Example: “Address” or “Product” or “Order” etc.<br>

2. #### What message Kafka consumer will receive?

Same message what we have send, only difference is payload is encoded as json String.
``` java
public class MessageEnvelop<String> {
String action;
String version;
String payload;
}
```
### Message serialization and deserialization.
When you send MessageEnvelop<?> to Kafka topic you must use “com.tiny.router.serialization.MessageEnvelopSerializer” to serialize Kafka message to byte array.<br>
Now when you receive message from kafka topic you should use “com.tiny.router.serialization.MessageEnvelopDeserializer” to deserialize to MessageEnvelop<String>

### Message conversion to the java target Type.
As mentioned earlier RrouteMap has information of method argument object Type.<br> 
Using Jackson ObjectMapper “tiny-router” convert payload json string to target Object.

### Message delegation to the appropriate handler to process the message.
When any routing path entry present in the RouteData,<br> “tiny-router” use java reflection to call target Method, and hence Message is properly dispatched.
   