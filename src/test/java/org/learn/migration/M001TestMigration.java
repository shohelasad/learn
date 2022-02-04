package org.learn.migration;

import javax.enterprise.inject.Vetoed;

import org.learn.migration.SimpleSchemaMigration;

@Vetoed
public class M001TestMigration extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "up";
	}
	
	@Override
	public String downQuery() {
		return "down";
	}

}
