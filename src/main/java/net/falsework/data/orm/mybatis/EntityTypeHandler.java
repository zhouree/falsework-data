package net.falsework.data.orm.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.falsework.data.orm.entity.IdEntity;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @param <Entity>
 */
@MappedTypes(value = { MybatisEntity.class })
@MappedJdbcTypes(value = { JdbcType.BIGINT })
public class EntityTypeHandler<Entity extends IdEntity> extends BaseTypeHandler<Entity> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private Class<Entity> type;
	
	public EntityTypeHandler(Class<Entity> type) {
		if (type == null)
			throw new IllegalArgumentException("Type argument cannot be null");
		this.type = type;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Entity parameter, JdbcType jdbcType) throws SQLException {
		ps.setLong(i, parameter.getId());
	}

	@Override
	public Entity getNullableResult(ResultSet rs, String columnName) throws SQLException {
		Long id = rs.getLong(columnName);
		return getEntityInstance(id);
	}

	@Override
	public Entity getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		Long id = rs.getLong(columnIndex);
		return getEntityInstance(id);
	}

	@Override
	public Entity getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		Long id = cs.getLong(columnIndex);
		return getEntityInstance(id);
	}

	protected Entity getEntityInstance(Long id) {
		try {
			Entity entity = type.newInstance();
			entity.setId(id);
			return entity;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
