package com.tiny.router.handler;

import com.tiny.router.annotation.RouteEntry;
import com.tiny.router.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressHandler3 implements MessageHandler{
    Logger log = LoggerFactory.getLogger(AddressHandler3.class);
    @RouteEntry(action = "update")
    public void handleAddress(Address address, Integer a){
      log.info(address.toString());
    }

}
