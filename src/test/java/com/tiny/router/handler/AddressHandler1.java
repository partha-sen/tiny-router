package com.tiny.router.handler;

import com.tiny.router.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddressHandler1 implements MessageHandler{
    Logger log = LoggerFactory.getLogger(AddressHandler1.class);
    public void handleAddress(Address address){
      log.info(address.toString());
    }

}
