package com.tiny.router.handler;

import com.tiny.router.annotation.RouteEntry;
import com.tiny.router.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressHandler2 implements MessageHandler{
    Logger log = LoggerFactory.getLogger(AddressHandler2.class);
    @RouteEntry(action = "update")
    public void handleAddress(Address address){
      log.info(address.toString());
    }

    @RouteEntry(action = "update")
    public void updateAddress(Address address){
        log.info(address.toString());
    }

}
