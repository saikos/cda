package com.github.saikos.security;

import com.github.saikos.object.StorageCoordinates;
import com.github.saikos.object.SubjectData;
import com.github.saikos.security.capabilities.Capability;
import com.github.saikos.security.capabilities.CapabilityBuilder;

import java.util.Map;

/**
 * Marker interface for capability builders that refer to a specific object
 * */
public interface ObjectSpecificCapabilityBuilder<
    S extends Subject<? extends SubjectData, ? extends AuthorizationAwareSubsystem<? extends EcosystemEnvironment, S>>,
    C extends Capability<S>
> extends CapabilityBuilder<S, C> {

    String COORDS = "coords";
    String STORENAME = "storeName";
    String ID = "id";

    C with(StorageCoordinates coords);

    default C with(String storeName, String id) {
        return with(StorageCoordinates.of(storeName, id));
    }

    @Override
    default C with(Map<String, ?> params) {
        if (params.size() == 1 && params.containsKey(COORDS)) {
            // let the exception flow
            if(params.get(COORDS) instanceof String coords) {
                return with(StorageCoordinates.decode(coords));
            } else {
                return with((StorageCoordinates) params.get(COORDS));
            }
        }
        if (params.size() == 2 && params.containsKey(STORENAME) && params.containsKey(ID)) {
            return with((String) params.get(STORENAME), (String) params.get(ID));
        }
        throw new RuntimeException("Invalid dynamic params: " + params + " for " + this);
    }
}