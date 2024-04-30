package com.tiny.router.handler;

import com.tiny.router.annotation.RouteEntry;
import com.tiny.router.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressHandler4 implements MessageHandler{
    Logger log = LoggerFactory.getLogger(AddressHandler4.class);
    @RouteEntry(action = "update", version = "1.0")
    public void handleAddress(Address address){
      log.info(address.toString());
    }

}
