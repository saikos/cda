package com.github.saikos.security.capabilities;

import com.github.saikos.object.SubjectData;
import com.github.saikos.security.EcosystemEnvironment;
import com.github.saikos.security.Subject;
import com.github.saikos.security.Subsystem;

import java.util.Collections;
import java.util.Map;

public class CapabilityProvision<
    S extends Subject<? extends SubjectData, ? extends Subsystem<? extends EcosystemEnvironment, S>>,
    C extends Capability<S>
> {

    public enum Result {
        UNRESOLVED,
        GRANTED,
        DENIED,
        ERROR
    }

    public final String name;
    public final Map<String, ?> params;
    private Result result;
    private C capability;
    private String errorMessage;

    public CapabilityProvision(String name, Map<String, ?> params) {
        this.name = name;
        this.params = Collections.unmodifiableMap(params);
        this.result = Result.UNRESOLVED;
    }

    public void granted(C capability) {
        if(result != Result.UNRESOLVED) {
            throw new RuntimeException("The capability provision has been already resolved");
        }
        this.result = Result.GRANTED;
        this.capability = capability;
    }

    public void denied() {
        if(result != Result.UNRESOLVED) {
            throw new RuntimeException("The capability provision has been already resolved");
        }
        this.result = Result.DENIED;

    }

    public void error(String errorMessage) {
        if(result != Result.UNRESOLVED) {
            throw new RuntimeException("The capability provision has been already resolved");
        }
        this.result = Result.ERROR;
        this.errorMessage = errorMessage;
    }

    public C getCapability() {
        return capability;
    }

//    //TODO hack to allow the value to be set to null in BuiCapabilitiesController as JsonIgnore of Jackson not working
//    public void setCapability(C capability) {
//        this.capability = capability;
//    }

    public Result getResult() {
        return result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
