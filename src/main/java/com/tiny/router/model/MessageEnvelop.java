package com.tiny.router.model;


import java.io.Serializable;

/**
 * MessageEnvelop encapsulate message in the payload field.
 * payload is a generic Type
 * @param <P> Represent generic Type
 */
public class MessageEnvelop<P> implements Serializable {
    /**
     * message action.
     */
    String action;

    /**
     * message payload version
     */
    String version;
    /**
     * message payload data.
     */
    P payload;

    /**
     * Default constructor
     */
    public MessageEnvelop() {
    }

    /**
     * Constructor
     * @param action message action.
     * @param payload message payload object.
     */

    public MessageEnvelop(String action, P payload) {
        this.action = action;
        this.payload = payload;
    }

    /**
     *
     * @param action action message action.
     * @param version message payload version.
     * @param payload message payload object.
     */

    public MessageEnvelop(String action, String version, P payload) {
        this.action = action;
        this.version = version;
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
     * This method calculate route path
     * @return route path
     */

    public String routePath() {
        String path = action;
        if(version != null){
            path = path + version;
        }
        return path;
    }

    /**
     * This method set action type
     * @param action represent message related to some action.
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     *
     * @return message payload version.
     */
    public String getVersion() {
        return version;
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
