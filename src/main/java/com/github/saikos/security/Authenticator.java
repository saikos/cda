package com.github.saikos.security;

public interface Authenticator<U extends Subject<?, ?>, R> {

    /**
     * Authenticates a request
     * @param request the request
     * @return a U object or null
     */
    U authenticate(R request);
}
