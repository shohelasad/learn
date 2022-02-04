package org.learn.migration;

import javax.enterprise.inject.Vetoed;

import org.learn.migration.SimpleSchemaMigration;

@Vetoed
public class NotAValidMigration extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "up";
	}
	
	@Override
	public String downQuery() {
		return "down";
	}

}
