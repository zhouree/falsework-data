package net.falsework.data.orm.mybatis;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

public class MapperScannerConfigurer implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

	private static Logger logger = LoggerFactory.getLogger(MapperScannerConfigurer.class);

	private ResourceLoader resourceLoader;
	private Environment environment;

	private String mapperPackages = "${orm.mybatis.mapper_packages:${orm.base_packages}}";

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);
		scanner.setEnvironment(environment);
		scanner.setResourceLoader(resourceLoader);
		scanner.setAnnotationClass(Mapper.class);
		scanner.registerFilters();
		try {
			logger.info("Found MyBatis auto-configuration package '{}'", mapperPackages);
			scanner.doScan(mapperPackages);
		} catch (IllegalStateException ex) {
			logger.debug("Could not determine auto-configuration " + "package, automatic mapper scanning disabled.");
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
