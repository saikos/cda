package com.github.saikos.security;

import com.github.saikos.object.SubjectData;

/**
 * This is an in-memory representation of either
 * - a User (e.g. an actual person)
 * - or an agent (e.g. a machine)
 * permitted to execute actions in a given subsystem of the application ecosystem,
 * according to their granted permissions and the capabilities that are derived
 * from these permissions.
 *
 */
public interface Subject<
    D extends SubjectData,
    F extends Subsystem<? extends EcosystemEnvironment, ? extends Subject<D, F>>
> {

    /** The name of the subject in our application environment (the subject's storage coordinates) */
    default String getName() {
        D dataObject = getData();
        return dataObject == null ?  null : dataObject.encodeCoordinates() ;
    }

    default boolean isAuthenticated() {
        return getData() != null;
    }

    default boolean isEnabled() {
        D dataObject = getData();
        if (dataObject == null) {
            return false;
        }
        else {
            try {
                return dataObject.isEnabled();
            }
            catch(Exception e) {
                return false;
            }
        }
    }

    /** When null, the subject is not authenticated */
    D getData();

    F getSubsystem();

    GrantedPermissions getGrantedPermissions();

    boolean isGrantedWriteCapableCustomPermissions();
//    {
//        return getGrantedPermissions().numberOfGrantedWriteCapableCustomPermissions() > 0;
//    }

    boolean anyReadCapableRoleIsGranted();

}
