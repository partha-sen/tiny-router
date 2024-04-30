package com.tiny.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiny.router.exception.*;
import com.tiny.router.handler.*;
import com.tiny.router.model.Address;
import com.tiny.router.model.MessageEnvelop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
public class MessageRouterTest {
    ObjectMapper mapper =new ObjectMapper();
    AddressHandler addressHandler = spy(new AddressHandler());
    Address address = new Address("Mark", "california", "Avalon Boulevard");

    MessageRouter<MessageHandler> messageRouter = null;

    @BeforeEach
    void init(){
        try {
            messageRouter = new MessageRouter<>(Collections.singletonList(addressHandler));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testRouteSuccess() {
        try {
            MessageEnvelop<String> mv = new MessageEnvelop<>("update", mapper.writeValueAsString(address));
            messageRouter.route(mv);
            Mockito.verify(addressHandler, Mockito.times(1)).handleAddress(any(Address.class));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }        
    }

    @Test
    public void testRouteSuccessWithVersion() {
        try {
            AddressHandler4 addressHandler = spy(new AddressHandler4());
            messageRouter = new MessageRouter<>(Collections.singletonList(addressHandler));
            MessageEnvelop<String> mv = new MessageEnvelop<>("update", "1.0", mapper.writeValueAsString(address));
            messageRouter.route(mv);
            Mockito.verify(addressHandler, Mockito.times(1)).handleAddress(any(Address.class));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRouteNotFound() {
        try {
            MessageEnvelop<String> mv = new MessageEnvelop<>("Insert", mapper.writeValueAsString(address));
            assertThrows(RouteNotFoundException.class, () -> messageRouter.route(mv));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPayloadConversionException() {
        try {
            MessageEnvelop<String> mv = new MessageEnvelop<>("update", "incorrect payload type");
            assertThrows(PayloadConversionException.class, () -> messageRouter.route(mv));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRouteEntryMissingException() {
        AddressHandler1 addressHandler = new AddressHandler1();
        assertThrows(RouteEntryMissingException.class, ()-> new MessageRouter<>(Collections.singletonList(addressHandler)));
    }

    @Test
    public void testDuplicateRouteException() {
        AddressHandler2 addressHandler = new AddressHandler2();
        assertThrows(DuplicateRouteException.class, ()-> new MessageRouter<>(Collections.singletonList(addressHandler)));
    }

    @Test
    public void testInvalidRoutingMethodSignatureException() {
        AddressHandler3 addressHandler = new AddressHandler3();
        assertThrows(InvalidRoutingMethodSignatureException.class, ()-> new MessageRouter<>(Collections.singletonList(addressHandler)));
    }

}
