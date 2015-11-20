/*
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.falsework.data.orm.mybatis;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import net.falsework.data.orm.config.OrmProperties;

/**
 * {@link EnableAutoConfiguration Auto-Configuration} for Mybatis. Contributes a
 * {@link SqlSessionFactory} and a {@link SqlSessionTemplate}.
 *
 * If {@link org.mybatis.spring.annotation.MapperScan} is used, or a configuration file is
 * specified as a property, those will be considered, otherwise this auto-configuration
 * will attempt to register mappers based on the interface definitions in or under the
 * root auto-configuration package.
 *
 * @author Eddú Meléndez
 * @author Josh Long
 */
@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@EnableConfigurationProperties(OrmProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration{

	private static Logger logger = LoggerFactory.getLogger(MybatisAutoConfiguration.class);
	
	private static final String CONFIG_LOCATION = "classpath:mybatis-config.xml";
	
	@Autowired
	private OrmProperties properties;

	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	/**
	 * Execution mode.
	 */
	private ExecutorType executorType = ExecutorType.SIMPLE;

	@PostConstruct
	public void checkConfigFileExists() {
		Resource resource = this.resourceLoader.getResource(CONFIG_LOCATION);
		Assert.state(resource.exists(),	"Cannot find config location: " + resource
				+ " (please add config file or check your Mybatis configuration)");
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setConfigLocation(this.resourceLoader.getResource(CONFIG_LOCATION));
		factory.setTypeAliasesSuperType(TypeAliases.class);
		String typeAliasesPackages = properties.getTypeAliasesPackages();
		if(typeAliasesPackages!=null){
			logger.info("MyBatis TypeAliasesPackages:{}",typeAliasesPackages);
			factory.setTypeAliasesPackage(typeAliasesPackages);
		}
		//省略TypeHandler扫描，默认只用一个EntityHandler
		//factory.setTypeHandlersPackage(this.properties.getPackagesToScan());
		//factory.setMapperLocations(this.properties.getMapperLocations());
		return factory.getObject();
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory,executorType);
	}
	
	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating
	 * instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this will
	 * bring in a bean registrar and automatically register components based on the same
	 * component-scanning path as Spring Boot itself.
	 */
	@Configuration
	@EnableConfigurationProperties(OrmProperties.class)
	@Import({ MapperScannerConfigurer.class })
	@ConditionalOnMissingBean(MapperFactoryBean.class)
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			logger.debug(String.format("No %s found.", MapperFactoryBean.class.getName()));
		}
	}

}
