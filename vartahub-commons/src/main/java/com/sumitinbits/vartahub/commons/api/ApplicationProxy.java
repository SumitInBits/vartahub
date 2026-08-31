package com.sumitinbits.vartahub.commons.api;

public interface ApplicationProxy {
    <T> T getApplicationClient(String applicationName, Class<T> serviceInterface);

    <T> T getApplicationClientNoLoadBalance(String serviceURL, Class<T> serviceInterface);
}
