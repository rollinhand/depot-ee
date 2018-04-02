package org.kivio.application;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.sql.DataSource;

@ApplicationScoped
public class LiquibaseProducer {
    @Resource(name = "depotDS")
    private DataSource dataSource;

    @Produces
    @LiquibaseType
    private CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("liquibase/changeset.xml");
        return config;
    }

    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }

    @Produces
    @LiquibaseType
    public DataSource createDataSource() {
        return dataSource;
    }
}
