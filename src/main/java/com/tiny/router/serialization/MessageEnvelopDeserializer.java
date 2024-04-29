package com.tiny.router.serialization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.model.MessageEnvelop;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class MessageEnvelopDeserializer implements Deserializer<MessageEnvelop<String>> {

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public MessageEnvelop<String> deserialize(String topic, byte[] bytes) {
        try {
            TypeReference<MessageEnvelop<String>> type = new TypeReference<MessageEnvelop<String>>(){};
            return  mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new SerializationException("Failed to convert byte[] to MessageEnvelop<String>", e);
        }

    }
}
