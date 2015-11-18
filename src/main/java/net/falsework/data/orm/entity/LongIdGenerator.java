package net.falsework.data.orm.entity;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class LongIdGenerator implements IdentifierGenerator {
	
	/**
	 * 调用Spring初始化的IdGenerator
	 */
	private static IdGenerator<Long> idGeneratorInstance;

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		return idGeneratorInstance.generate();
	}

	protected static void setIdGeneratorInstance(IdGenerator<Long> idGeneratorInstance) {
		LongIdGenerator.idGeneratorInstance = idGeneratorInstance;
	}

}
