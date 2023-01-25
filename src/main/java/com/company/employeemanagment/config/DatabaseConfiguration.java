package com.company.employeemanagment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.company.employeemanagment.repository",
        entityManagerFactoryRef = "emp-em",
        transactionManagerRef = "emp-tm"
)
public class DatabaseConfiguration {
    @Value("${emp.datasource.username}")
    private String datasourceUser;
    @Value("${emp.datasource.password}")
    private String datasourcePassword;
    @Value("${emp.datasource.url}")
    private String datasourceHost;
    @Value("${emp.datasource.driver}")
    private String datasourceDriver;
    @Value("${emp.hibernate.database-platform}")
    private String hibernateDatabasePlatform;
    @Value("${emp.hibernate.show-sql}")
    private boolean hibernateShowSql;
    @Value("${emp.hibernate.format-sql}")
    private boolean hibernateFormatSql;
    @Value("${emp.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Bean
    public DataSource dataSourceSphere() {
        HikariConfig config = new HikariConfig();
        config.setUsername(datasourceUser);
        config.setPassword(datasourcePassword);
        config.setJdbcUrl(datasourceHost);
        config.setDriverClassName(datasourceDriver);
        return new HikariDataSource(config);
    }

    @Bean(name = "emp-em")
    public LocalContainerEntityManagerFactoryBean entityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceSphere());
        em.setPackagesToScan("com.company.employeemanagment.model");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", hibernateDdlAuto);
        properties.put("hibernate.dialect", hibernateDatabasePlatform);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.format_sql", hibernateFormatSql);
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean(name = "emp-tm")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManager().getObject());
        return transactionManager;
    }
}
