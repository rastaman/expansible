package io.expansible;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import io.dropwizard.Application;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.expansible.core.InventoriesApiService;
import io.expansible.resources.InventoriesApi;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class ExpansibleApplication extends Application<ExpansibleConfiguration> {

	public static void main(final String[] args) throws Exception {
		new ExpansibleApplication().run(args);
	}

	@Override
	public String getName() {
		return "Expansible";
	}

	@Override
	public void initialize(final Bootstrap<ExpansibleConfiguration> bootstrap) {
		bootstrap.addBundle(new Java8Bundle());
        bootstrap.addBundle(new TemplateConfigBundle());
        final ExpansibleModule guiceModule = new ExpansibleModule();
        bootstrap.addBundle(guiceModule.getMigrationBundle());
        bootstrap.addBundle(guiceModule.getHibernateBundle());
        bootstrap.addBundle(guiceModule.getGuiceBundle());
        bootstrap.addBundle(guiceModule.getSwaggerBundle());
        bootstrap.addBundle(new SwaggerBundle<ExpansibleConfiguration>() {
          @Override
          protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ExpansibleConfiguration configuration) {
              return configuration.swaggerBundleConfiguration;
          }
      });
        bootstrap.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Map requests to /dashboard/${1} to be found in the class path at /assets/${1}.
        bootstrap.addBundle(new ConfiguredAssetsBundle(
            ImmutableMap.<String, String>builder()
                .put("/assets/", "/dashboard/")
                //.put("/data/", "/static-data/")
                .build()));
	}

	@Override
	public void run(final ExpansibleConfiguration configuration, final Environment environment) {
	  InventoriesApiService service = new InventoriesApiService(configuration);
	  InventoriesApi api = new InventoriesApi(service);
    environment.jersey().register(api);
	}

}
