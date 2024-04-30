package com.tiny.router.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.model.Address;
import com.tiny.router.model.MessageEnvelop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SerializationTest {


    ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testSerialization() throws JsonProcessingException {

        try (MessageEnvelopSerializer<Address> ms = new MessageEnvelopSerializer<>()) {
            Address address = new Address("Partha", "Telangana", "DK enclave");
            MessageEnvelop<Address> envelop = new MessageEnvelop<>("update", address);
            byte[] data = ms.serialize("topic", envelop);
            try (MessageEnvelopDeserializer med = new MessageEnvelopDeserializer()) {
                MessageEnvelop<String> messageEnvelop = med.deserialize("topic", data);
                assertEquals(envelop.getAction(), messageEnvelop.getAction());
                assertEquals(mapper.writeValueAsString(envelop.getPayload()), messageEnvelop.getPayload());
            }
        }

    }

}
