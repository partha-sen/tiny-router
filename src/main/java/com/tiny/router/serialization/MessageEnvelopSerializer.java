package com.tiny.router.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.model.MessageEnvelop;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;

public class MessageEnvelopSerializer<P extends Serializable>  implements Serializer<MessageEnvelop<P>> {

    ObjectMapper mapper = new ObjectMapper();
    @Override
    public byte[] serialize(String topic, MessageEnvelop<P> envelop) {
        try {
            String jsonPayload = mapper.writeValueAsString(envelop.getPayload());
            MessageEnvelop<String> serializerMessage = new MessageEnvelop<>(envelop.getAction(), envelop.getVersion(), jsonPayload);
            return mapper.writeValueAsBytes(serializerMessage);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Failed to serializing MessageEnvelop<?> to byte[]", e);
        }
    }
}
