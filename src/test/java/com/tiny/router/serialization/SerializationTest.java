package com.tiny.router.serialization;

import com.tiny.router.envelop.MessageEnvelop;
import org.junit.jupiter.api.Test;


public class SerializationTest {


    @Test
    public void testSerialization(){

        try (MessageEnvelopSerializer<Address> ms = new MessageEnvelopSerializer<>()) {
            Address a = new Address("Partha", "Telangana", "DK enclave");
            MessageEnvelop<Address> envelop = new MessageEnvelop<>("update", a);
            byte[] data = ms.serialize("topic", envelop);
            try (MessageEnvelopDeserializer med = new MessageEnvelopDeserializer()) {
                MessageEnvelop<String> messageEnvelop = med.deserialize("topic", data);
                System.out.println("action = "+messageEnvelop.getAction());
                System.out.println("payload = "+messageEnvelop.getPayload());
            }
        }

    }

}
