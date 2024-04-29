package com.tiny.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.annotation.RouteEntry;
import com.tiny.router.model.MessageEnvelop;
import com.tiny.router.model.RouteValue;
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
    private final Map<String, RouteValue<T>>  routeMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();


    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);

    /**
     * @param actionHandlers is a list of all message handler class
     */
    public MessageRouter(List<T> actionHandlers) {

        for (T actionHandler : actionHandlers) {
            boolean isRouteEntryAnnotationPresent = false;
            Method[] methods = actionHandler.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RouteEntry.class)) {
                    if (method.getParameters().length == 1) {
                        RouteEntry annotation = method.getAnnotation(RouteEntry.class);
                        Class<?> firstParameterType = method.getParameterTypes()[0];
                        RouteValue<T> routeValue = new RouteValue<>(actionHandler, method, firstParameterType);
                        this.routeMap.put(annotation.action() + annotation.version(), routeValue);
                        isRouteEntryAnnotationPresent = true;
                        break;
                    }else {
                        throw new RuntimeException("Method: "+method.getName()+" of Class: "+actionHandler.getClass().getName() +" must contain single argument.");
                    }
                }
            }
            if (!isRouteEntryAnnotationPresent) {
                throw new RuntimeException("PayloadAction annotation missing on method of " + actionHandler.getClass());
            }
        }
    }

    /**
     * @param msgEnvelop MessageEnvelop field action contain payload as string.
     * @throws InvocationTargetException will define in future
     * @throws IllegalAccessException    will define in future
     */
    public void route(MessageEnvelop<String> msgEnvelop) throws InvocationTargetException, IllegalAccessException {
        RouteValue<T> routeValue = routeMap.get(msgEnvelop.getAction());
        Method method = routeValue.getMethod();
        Class<?> parameterType = routeValue.getParameterType();
        T instance = routeValue.getInstance();
        Object payload = toPayloadType(msgEnvelop.getPayload(), parameterType);
        method.invoke(instance, payload);
    }


    /**
     * This method convert object Type
     *
     * @param payload represent payload String.
     * @param payloadClass type of payload
     * @return Object of type payloadClass
     */
    private Object toPayloadType(String payload, Class<?> payloadClass) {
        try {
            return mapper.readValue(payload, payloadClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Fail to convert payload to " + payloadClass.getTypeName(), e);
        }
    }
}
