package net.falsework.data.orm.hibernate;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import net.falsework.data.orm.config.OrmProperties;

@Configuration
@ConditionalOnClass({ SessionFactory.class,HibernateTransactionManager.class})
@EnableConfigurationProperties(OrmProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class HibernateAutoConfiguration{

	private static Logger logger = LoggerFactory.getLogger(HibernateAutoConfiguration.class);

	@Autowired
	private OrmProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public SessionFactory sessionFactory(DataSource dataSource) throws Exception {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		LocalSessionFactoryBuilder sfb = new LocalSessionFactoryBuilder(dataSource, resourcePatternResolver);
		if (properties.getHibernateProperties() != null) {
			sfb.addProperties(properties.getHibernateProperties());
		}
		String packagesToScan = properties.getHibernatePackagesToScan();
		if (packagesToScan != null) {
			logger.info("Hibernate PackagesToScan:{}",packagesToScan);
			sfb.scanPackages(packagesToScan);
		}
		// Build SessionFactory instance.
		return sfb.buildSessionFactory();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) throws Exception {
		logger.info("Configuration HibernateTransactionManager.");
		return new HibernateTransactionManager(sessionFactory);
	}
	
}
