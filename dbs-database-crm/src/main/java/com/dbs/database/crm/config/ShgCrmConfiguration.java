package com.dbs.database.crm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.apache.commons.dbcp2.BasicDataSource;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.dbs.database.crm"},
        entityManagerFactoryRef = "crmEntityManager",
        transactionManagerRef = "crmTransactionManager")
@PropertySource("classpath:application.yml")
@EnableTransactionManagement
public class ShgCrmConfiguration {
    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties
    public BasicDataSource crmDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        ds.setUrl(env.getProperty("spring.datasource.url"));
        ds.setUsername(env.getProperty("spring.crm-datasource.userName"));
        ds.setPassword(env.getProperty("spring.crm-datasource.password"));

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean crmEntityManager() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        BasicDataSource ds = crmDataSource();
        ds.setUsername(env.getProperty("spring.crm-datasource.userName"));

        em.setDataSource(ds);
        em.setPackagesToScan(
                new String[] { "com.dbs.database.crm"});
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setDatabase(Database.ORACLE);
        em.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.hibernate.ddl-auto"));
        properties.put("hibernate.implicit_naming_strategy", env.getProperty("spring.hibernate.implicit_naming_strategy"));
        properties.put("hibernate.physical_naming_strategy",
                env.getProperty("spring.hibernate.physical_naming_strategy"));
        properties.put("hibernate.dialect",
                env.getProperty("spring.hibernate.dialect"));
        properties.put("hibernate.show_sql",
                env.getProperty("spring.hibernate.show_sql"));

        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager crmTransactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                crmEntityManager().getObject());
        return transactionManager;
    }
    
    @Bean(name = "crmEntityManagerInstance")
    @Primary
    public EntityManager crmEntityManagerInstance(@Qualifier("crmEntityManager") LocalContainerEntityManagerFactoryBean factoryBean) {
        return factoryBean.getObject().createEntityManager();
    }
    

}
