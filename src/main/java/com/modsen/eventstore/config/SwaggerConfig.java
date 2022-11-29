package com.modsen.eventstore.config;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.modsen.eventstore.dto.criteria.event.EventCriteria;
import com.modsen.eventstore.dto.criteria.event.EventCriteriaField;
import com.modsen.eventstore.dto.criteria.event.EventFilterCriteria;
import com.modsen.eventstore.dto.criteria.event.EventSortingCriteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {

    @Value("${swagger.title}")
    String title;

    @Value("${swagger.description}")
    String description;

    @Value("${swagger.version}")
    String version;

    @Bean
    public Docket docket(TypeResolver typeResolver) {
        ResolvedType[] additionalModels = {
                typeResolver.resolve(EventCriteriaField.class),
                typeResolver.resolve(EventFilterCriteria.class),
                typeResolver.resolve(EventSortingCriteria.class),
        };

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(title)
                        .description(description)
                        .version(version)
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .additionalModels(typeResolver.resolve(EventCriteria.class), additionalModels);
    }

}