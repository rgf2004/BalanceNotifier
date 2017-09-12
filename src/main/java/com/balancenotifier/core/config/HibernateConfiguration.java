package com.balancenotifier.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.balancenotifier.core.config" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

	@Autowired
	private ConfigUtil configUtil;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] { "com.balancenotifier.data.model" });
		sessionFactory.setHibernateProperties(hibernateProperties());
		return sessionFactory;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(configUtil.getEnvironment().getRequiredProperty("jdbc.driverClassName"));
		dataSource.setUrl(configUtil.getEnvironment().getRequiredProperty("jdbc.url"));
		dataSource.setUsername(configUtil.getEnvironment().getRequiredProperty("jdbc.username"));
		dataSource.setPassword(configUtil.getEnvironment().getRequiredProperty("jdbc.password"));
		return dataSource;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", configUtil.getEnvironment().getRequiredProperty("hibernate.dialect"));
		properties.put("hibernate.show_sql", configUtil.getEnvironment().getRequiredProperty("hibernate.show_sql"));
		properties.put("hibernate.format_sql", configUtil.getEnvironment().getRequiredProperty("hibernate.format_sql"));
		properties.put("hibernate.hbm2ddl.auto", configUtil.getEnvironment().getRequiredProperty("hibernate.hbm2ddl.auto"));
		properties.put("hibernate.enable_lazy_load_no_trans", configUtil.getEnvironment().getRequiredProperty("hibernate.enable_lazy_load_no_trans"));		
		return properties;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory s) {

		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);
		return txManager;
	}
}