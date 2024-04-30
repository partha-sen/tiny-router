package com.tiny.router.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.model.MessageEnvelop;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

/**
 * MessageEnvelopSerializer serialize Kafka message.
 * @param <P> custom message object.
 */
public class MessageEnvelopSerializer<P>  implements Serializer<MessageEnvelop<P>> {

    ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param topic kafka topic.
     * @param envelop generic MessageEnvelop object.
     * @return byte array.
     */
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
