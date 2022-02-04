package org.learn.migration.util;

import static br.com.caelum.vraptor.environment.EnvironmentType.DEVELOPMENT;

import java.io.IOException;

import org.learn.providers.LearnDatabaseConfiguration;

import br.com.caelum.vraptor.environment.DefaultEnvironment;


public class SchemaUpdateGenerator {

	public static void main(String[] args) throws IOException {
		LearnDatabaseConfiguration mamuteConfiguration = new LearnDatabaseConfiguration(new DefaultEnvironment(DEVELOPMENT), null, null);
		mamuteConfiguration.init();
		mamuteConfiguration.getSchemaUpdate().execute(true, false);
	}

}
