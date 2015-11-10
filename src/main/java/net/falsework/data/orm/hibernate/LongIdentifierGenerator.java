package net.falsework.data.orm.hibernate;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

public class LongIdentifierGenerator implements IdentifierGenerator{

	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

}
