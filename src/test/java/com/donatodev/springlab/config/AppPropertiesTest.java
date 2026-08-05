package com.donatodev.springlab.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class AppPropertiesTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withUserConfiguration(PropertiesConfiguration.class)
                    .withPropertyValues(
                            "app.environment=test",
                            "app.message=Test configuration loaded",
                            "app.owner=Test Owner"
                    );

    @Test
    void bindsAppProperties() {
        contextRunner.run(context -> {
            AppProperties properties = context.getBean(AppProperties.class);

            assertThat(properties.environment()).isEqualTo("test");
            assertThat(properties.message()).isEqualTo("Test configuration loaded");
            assertThat(properties.owner()).isEqualTo("Test Owner");
        });
    }

    @Configuration(proxyBeanMethods = false)
    @ConfigurationPropertiesScan(basePackageClasses = AppProperties.class)
    static class PropertiesConfiguration {
    }
}
