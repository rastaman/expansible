package io.expansible;

import java.time.Clock;

import javax.ws.rs.client.Client;

import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.guice.lazy.LazySingleton;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;


public class ExpansibleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SessionFactory.class).toProvider(hibernateBundle::getSessionFactory);
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
    }

    @Provides
    @LazySingleton
    public final Client provideClient (Environment environment, ExpansibleConfiguration configuration) {
        return new JerseyClientBuilder(environment)
                .using(configuration.getClient())
                .build("test client");
    }

    public final GuiceBundle<ExpansibleConfiguration> getGuiceBundle() {
        return guiceGovernatorBundle;
    }

    public final HibernateBundle<ExpansibleConfiguration> getHibernateBundle() {
        return hibernateBundle;
    }

    public final MigrationsBundle<ExpansibleConfiguration> getMigrationBundle() {
        return migrationsBundle;
    }

    public final SwaggerBundle<ExpansibleConfiguration> getSwaggerBundle() {
        return swaggerBundle;
    }

    private final HibernateBundle<ExpansibleConfiguration> hibernateBundle =
        new ScanningHibernateBundle<ExpansibleConfiguration>("io.expansible.model") {
            @Override
            public DataSourceFactory getDataSourceFactory(ExpansibleConfiguration configuration) {
                return configuration.getDataSource();
            }
        };

    private final MigrationsBundle<ExpansibleConfiguration> migrationsBundle =
        new MigrationsBundle<ExpansibleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ExpansibleConfiguration configuration) {
                return configuration.getDataSource();
            }
        };

    private final GuiceBundle<ExpansibleConfiguration> guiceGovernatorBundle = GuiceBundle.<ExpansibleConfiguration>newBuilder()
        .addModule(this)
        .enableAutoConfig("io.expansible","trunk.dropwizard.newrelic")
        .setConfigClass(ExpansibleConfiguration.class)
        .setInjectorFactory((stage, modules) -> LifecycleInjector.builder()
            .inStage(stage)
            .withModules(modules)
            .build()
            .createInjector())
        .build();

    private final SwaggerBundle<ExpansibleConfiguration> swaggerBundle = new SwaggerBundle<ExpansibleConfiguration>() {
        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ExpansibleConfiguration configuration) {
            return configuration.swaggerBundleConfiguration;
        }
    };
}
