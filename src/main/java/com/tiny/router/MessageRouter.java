package com.tiny.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.annotation.PayloadAction;
import com.tiny.router.envelop.MessageEnvelop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T> Represent message Type
 */

public class MessageRouter<T> {
    private final Map<String, Method> actionMethods = new HashMap<>();
    private final Map<String, T> actionObjects = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();


    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);

    /**
     * @param actionHandlers is a list of all message handler class
     */
    public MessageRouter(List<T> actionHandlers) {

        for (T actionHandler : actionHandlers) {
            boolean isPayloadActionAnnotationPresent = false;
            Method[] methods = actionHandler.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(PayloadAction.class)) {
                    if (method.getParameters().length == 0) {
                        PayloadAction annotation = method.getAnnotation(PayloadAction.class);
                        this.actionMethods.put(annotation.value(), method);
                        this.actionObjects.put(annotation.value(), actionHandler);
                        isPayloadActionAnnotationPresent = true;
                        break;
                    }else {
                        throw new RuntimeException("Method: "+method.getName()+" of Class: "+actionHandler.getClass().getName() +" must contain single argument.");
                    }
                }
            }
            if (!isPayloadActionAnnotationPresent) {
                throw new RuntimeException("PayloadAction annotation missing on method of " + actionHandler.getClass());
            }
        }
    }

    /**
     * @param message Message json object as String.
     * @throws InvocationTargetException will define in future
     * @throws IllegalAccessException    will define in future
     */
    public void route(String message) throws InvocationTargetException, IllegalAccessException {
        MessageEnvelop<?> msgEnvelop = toMessageEnvelop(message);
        String action = msgEnvelop.getAction();
        Method method = actionMethods.get(action);
        Class<?> parameterType = method.getParameterTypes()[0];
        Object payload = toPayloadType(msgEnvelop.getPayload(), parameterType);
        method.invoke(actionObjects.get(action), payload);
    }

    /**
     * @param msgEnvelop MessageEnvelop field action contain payload as string.
     * @throws InvocationTargetException will define in future
     * @throws IllegalAccessException    will define in future
     */
    public void route(MessageEnvelop<String> msgEnvelop) throws InvocationTargetException, IllegalAccessException {
        String action = msgEnvelop.getAction();
        Method method = actionMethods.get(action);
        Class<?> parameterType = method.getParameterTypes()[0];
        Object payload = toPayloadType(msgEnvelop.getPayload(), parameterType);
        method.invoke(actionObjects.get(action), payload);
    }


    /**
     * @param message represent json string
     * @return MessageEnvelop object
     */
    private MessageEnvelop<?> toMessageEnvelop(String message) {
        try {
            return mapper.readValue(message, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.info("Json processing exception {}", e.getMessage());
            throw new RuntimeException("Invalid message format ", e);
        }
    }

    /**
     * This method convert object Type
     *
     * @param payload represent payload object.
     * @param payloadClass type of payload
     * @return Object of type payloadClass
     */
    private Object toPayloadType(Object payload, Class<?> payloadClass) {
        try {
            return mapper.convertValue(payload, payloadClass);
        } catch (RuntimeException e) {
            throw new RuntimeException("Fail to convert payload to " + payloadClass.getTypeName(), e);
        }
    }


}
