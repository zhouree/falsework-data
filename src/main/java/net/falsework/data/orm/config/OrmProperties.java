package net.falsework.data.orm.config;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * Configuration properties for Data.
 */
@ConfigurationProperties(prefix = OrmProperties.PREFIX)
public class OrmProperties implements BeanFactoryAware {

	public static final String PREFIX = "orm";

	private BeanFactory beanFactory;

	private String basePackages;

	private Hibernate hibernate = new Hibernate();

	private Mybatis mybatis = new Mybatis();

	/**
	 * 若不设置group值，将返回Spring Boot Application所在的包 默认只需把Spring Boot
	 * Application所在的类放在上一层的包中即可
	 */
	public String getBasePackages() {
		if (basePackages != null) {
			return basePackages;
		}
		try {
			return AutoConfigurationPackages.get(beanFactory).get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public Hibernate getHibernate() {
		return hibernate;
	}

	public void setHibernate(Hibernate hibernate) {
		this.hibernate = hibernate;
	}

	public Mybatis getMybatis() {
		return mybatis;
	}

	public void setMybatis(Mybatis mybatis) {
		this.mybatis = mybatis;
	}

	public Properties getHibernateProperties() {
		return this.hibernate.getProperties();
	}

	public String getHibernatePackagesToScan() {
		if (this.hibernate.getPackagesToScan() == null) {
			return getBasePackages();
		}
		return this.hibernate.getPackagesToScan();
	}

	public String getTypeAliasesPackages() {
		if (this.mybatis.getTypeAliasPackages() == null) {
			return getBasePackages();
		}
		return this.mybatis.getTypeAliasPackages();
	}

	public static class Mybatis {

		private String mapperPackages;
		private String typeAliasPackages;

		public String getTypeAliasPackages() {
			return typeAliasPackages;
		}

		public void setTypeAliasPackages(String typeAliasPackages) {
			this.typeAliasPackages = typeAliasPackages;
		}

		public String getMapperPackages() {
			return mapperPackages;
		}

		public void setMapperPackages(String mapperPackages) {
			this.mapperPackages = mapperPackages;
		}
	}

	public static class Hibernate {
		private String showSql;
		private String formatSql;
		private String ddlAuto;
		private String packagesToScan;

		private Properties properties = new Properties();

		public String getShowSql() {
			return showSql;
		}

		public Properties getProperties() {
			if (StringUtils.hasText(showSql)) {
				properties.put("hibernate.show_sql", showSql);
			}
			if (StringUtils.hasText(formatSql)) {
				properties.put("hibernate.format_sql", formatSql);
			}
			if (StringUtils.hasText(ddlAuto)) {
				properties.put("hibernate.hbm2ddl.auto", ddlAuto);
			}
			return properties;
		}

		public void setShowSql(String showSql) {
			this.showSql = showSql;
		}

		public String getFormatSql() {
			return formatSql;
		}

		public void setFormatSql(String formatSql) {
			this.formatSql = formatSql;
		}

		public String getDdlAuto() {
			return ddlAuto;
		}

		public void setDdlAuto(String ddlAuto) {
			this.ddlAuto = ddlAuto;
		}

		public String getPackagesToScan() {
			return packagesToScan;
		}

		public void setPackagesToScan(String packagesToScan) {
			this.packagesToScan = packagesToScan;
		}
	}
}
