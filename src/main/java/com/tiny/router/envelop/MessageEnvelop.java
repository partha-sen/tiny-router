package com.tiny.router.envelop;


import java.io.Serializable;

/**
 * MessageEnvelop encapsulate message in the payload field.
 * payload is a generic Type
 * @param <P> Represent generic Type
 */
public class MessageEnvelop<P> implements Serializable {

    String action;
    P payload;

    public MessageEnvelop() {
    }

    public MessageEnvelop(String action, P payload) {
        this.action = action;
        this.payload = payload;
    }

    /**
     * This method returns action type
     * @return returns action type
     */
    public String getAction() {
        return action;
    }

    /**
     * This method set action type
     * @param action represent message related to some action.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * This method returns payload.
     * @return payload message object.
     */
    public P getPayload() {
        return payload;
    }

    /**
     * This method set payload.
     * @param payload represent genetic message object
     */
    public void setPayload(P payload) {
        this.payload = payload;
    }
}
