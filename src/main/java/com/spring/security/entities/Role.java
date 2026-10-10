package com.spring.security.entities;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
	ADMIN(Set.of(Permissions.read,Permissions.delete,Permissions.write)),
	USER(Set.of(Permissions.read));
	
	private final Set<Permissions> permissions;
	
//	Role(Set<Permissions> permissions)
//	{
//		this.permissions=permissions;
//	}

}
