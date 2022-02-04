package org.learn.controllers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.learn.auth.rules.PermissionRules;

@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentAccessLevel {
	PermissionRules value();
}
