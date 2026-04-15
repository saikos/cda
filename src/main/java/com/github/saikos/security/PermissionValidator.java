package com.github.saikos.security;

import com.github.saikos.object.SubjectData;

public interface PermissionValidator<G extends GrantedPermissions> {

    G validateGrantedPermissions(SubjectData subjectData);

}
