package com.ab.auth.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
public class AuthDataSourceProperties {

    @Autowired
    private Environment environment;

    @Value("${current.database}")
    private String currentDatabase;

    public static Map<String, String> getJPAProperties(JPAProperties jpaProperties) {
        Map<String, String> properties = new HashMap<>();
        properties.put(AvailableSettings.SHOW_SQL, jpaProperties.getShowSQL());
        properties.put(AvailableSettings.HBM2DDL_AUTO, jpaProperties.getDdlAuto());
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
        properties.put(AvailableSettings.DIALECT, jpaProperties.getSqlDialect());
        return properties;
    }

    private String getDialect() {
        return environment.getProperty(String.format("spring.jpa.properties.%s.hibernate.dialect", currentDatabase));
    }
}


