package com.modsen.eventstore.containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    public static final String IMAGE_VERSION = "postgres:14";
    public static final String DATABASE_NAME = "test";
    public static PostgreSQLContainer container;

    public PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer()
                    .withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", container.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", container.getUsername());
        System.setProperty("TEST_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
    }

}