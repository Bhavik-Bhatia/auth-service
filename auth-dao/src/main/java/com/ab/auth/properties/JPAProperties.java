package com.ab.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class JPAProperties {
    @Value("${spring.jpa.show-sql}")
    private String showSQL;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;
    @Value("${spring.jpa.properties.pgsql.hibernate.dialect}")
    private String sqlDialect;
}
