package org.learn.migration.all;

import javax.enterprise.context.ApplicationScoped;

import org.learn.migration.MigrationOperation;
import org.learn.migration.RawSQLOperation;
import org.learn.migration.SchemaMigration;

import java.util.List;

@ApplicationScoped
public class M016AddUserToBlockedIp implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String q1 = "alter table BlockedIp " +
				" add column author_id bigint";
		String q2 = "alter table BlockedIp" +
				" add index FK_jx0qq5i8p02d6geh0cp4yl2lh (author_id)," +
				" add constraint FK_jx0qq5i8p02d6geh0cp4yl2lh" +
				" foreign key (author_id)" +
				" references Users (id)";

		return RawSQLOperation.forSqls(q1, q2);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
