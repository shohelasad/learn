package org.learn.migration.all;

import javax.enterprise.context.ApplicationScoped;

import org.learn.migration.MigrationOperation;
import org.learn.migration.RawSQLOperation;
import org.learn.migration.SchemaMigration;

import java.util.List;

@ApplicationScoped
public class M014AddUserDeleteColumn implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String q1 = "alter table Users add column deleted tinyint(1) default false";
		String q2 = "update Users set deleted=false";

		return RawSQLOperation.forSqls(q1, q2);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
