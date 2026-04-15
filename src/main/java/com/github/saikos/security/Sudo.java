package com.github.saikos.security;

import java.util.function.Function;

public interface Sudo<U extends Subject<?, ?>> {
    <O> O withSystemUser(Function<U, O> action);
}
