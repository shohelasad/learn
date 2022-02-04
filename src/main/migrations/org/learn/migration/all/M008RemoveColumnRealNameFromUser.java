package org.learn.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.learn.migration.MigrationOperation;
import org.learn.migration.RawSQLOperation;
import org.learn.migration.SchemaMigration;

@ApplicationScoped
public class M008RemoveColumnRealNameFromUser implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls("alter table Users drop realName");
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
