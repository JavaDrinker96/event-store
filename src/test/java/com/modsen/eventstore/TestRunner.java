package com.modsen.eventstore;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
        "com.modsen.eventstore.controller",
        "com.modsen.eventstore.mapper",
        "com.modsen.eventstore.service",
        "com.modsen.eventstore.repository",
})
public class TestRunner {
}