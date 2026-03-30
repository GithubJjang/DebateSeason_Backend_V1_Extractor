package com.debateseason.extractor.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
	basePackages = "com.debateseason.extractor.media.processed",
	transactionManagerRef = "processedDataTransactionManager",
	entityManagerFactoryRef = "processedDataEntityManagerFactory")
@EntityScan("com.debateseason.extractor.media.raw.processed")
public class DebateSeasonSourceConfig {

	@Bean(name = "processedDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.processed")
	public DataSource debateDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "processedDataEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean debateEntityManagerFactory(
		EntityManagerFactoryBuilder builder,
		@Qualifier("processedDataSource") DataSource dataSource) {

		return builder
			.dataSource(dataSource)
			.packages("com.debateseason.extractor.media.processed.entity")
			.persistenceUnit("processed")
			.properties(jpaProps())
			.build();

	}

	@Bean(name = "processedDataTransactionManager")
	public PlatformTransactionManager debateTransactionManager(
		@Qualifier("processedDataEntityManagerFactory") EntityManagerFactory emf
	) {
		return new JpaTransactionManager(emf);
	}

	private Map<String, Object> jpaProps() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.dialect", "org.hibernate.dialect.MariaDBDialect");
		props.put("hibernate.hbm2ddl.auto", "update");
		//props.put("hibernate.show_sql", true);
		props.put("hibernate.format_sql", true);
		return props;
	}

}
