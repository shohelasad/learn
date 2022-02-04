package org.learn.migration.all;

import javax.enterprise.context.ApplicationScoped;

import org.learn.migration.MigrationOperation;
import org.learn.migration.RawSQLOperation;
import org.learn.migration.SchemaMigration;

import java.util.List;

@ApplicationScoped
public class M015CreateBlockedIpTable implements SchemaMigration {

	@Override
	public List<MigrationOperation> up() {
		String q1 = " create table BlockedIp (\n" +
				"        id bigint not null auto_increment,\n" +
				"        createdAt datetime,\n" +
				"        ip varchar(255),\n" +
				"        primary key (id)\n" +
				"    ) ENGINE=InnoDB";

		return RawSQLOperation.forSqls(q1);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
