package com.tiny.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.annotation.RouteEntry;
import com.tiny.router.exception.*;
import com.tiny.router.model.MessageEnvelop;
import com.tiny.router.model.RouteData;
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
    private final Map<String, RouteData<T>>  routeMap = new HashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();


    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);

    /**
     * Constructor definition
     * @param actionHandlers list of actionHandlers object.
     * @throws DuplicateRouteException when duplicate route with RouteEntry annotation.
     * @throws InvalidRoutingMethodSignatureException when invalid routing method signature found.
     * @throws RouteEntryMissingException when no route is defined by using RouteEntry annotation.
     */
    public MessageRouter(List<T> actionHandlers) throws DuplicateRouteException, InvalidRoutingMethodSignatureException, RouteEntryMissingException{

        for (T actionHandler : actionHandlers) {
            boolean isRouteEntryAnnotationPresent = false;
            Method[] methods = actionHandler.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RouteEntry.class)) {
                    if (method.getParameters().length == 1) {
                        RouteEntry annotation = method.getAnnotation(RouteEntry.class);
                        Class<?> firstParameterType = method.getParameterTypes()[0];
                        RouteData<T> routeData = new RouteData<>(actionHandler, method, firstParameterType);
                        RouteData<T> oldRouteData = this.routeMap.put(annotation.action() + annotation.version(), routeData);
                        if(oldRouteData != null){
                            String message = String.format("Duplicate route entry for the Action %s and Version %s", annotation.action(), annotation.version());
                            log.info(message);
                            throw new DuplicateRouteException(message);
                        }
                        isRouteEntryAnnotationPresent = true;
                    }else {
                        String message = String.format("Method: %s must contain single argument.",  method.toGenericString());
                        log.info(message);
                        throw new InvalidRoutingMethodSignatureException(message);
                    }
                }
            }
            if (!isRouteEntryAnnotationPresent) {
                String message = String.format("RouteEntry annotation missing on method of Class: %s",  actionHandler.getClass());
                log.info(message);
                throw new RouteEntryMissingException(message);
            }
        }
    }

    /**
     * @param msgEnvelop MessageEnvelop.
     * @throws RouteNotFoundException when no route is configured.
     * @throws MethodInvocationException when fail to call method through java reflection.
     * @throws PayloadConversionException when Fail to convert payload to object Type.
     */
    public void route(MessageEnvelop<String> msgEnvelop) {

        String routPath = msgEnvelop.routePath();
        RouteData<T> routeData = routeMap.get(routPath);
        if(routeData == null ){
            String message = String.format("Route not configured for action %s and version %s", msgEnvelop.getAction(), msgEnvelop.getVersion());
            log.info(message);
            throw new RouteNotFoundException(message);
        }
        Method method = routeData.getMethod();
        Class<?> parameterType = routeData.getParameterType();
        T instance = routeData.getInstance();
        Object payload = toPayloadType(msgEnvelop.getPayload(), parameterType);
        try {
            method.invoke(instance, payload);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("Method invocation failed", e);
            throw new MethodInvocationException("Method invocation failed ", e);
        }
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
            String message = String.format("Fail to convert json payload to object Type %s",  payloadClass.getTypeName());
            log.info(message);
            throw new PayloadConversionException(message, e);
        }
    }
}
