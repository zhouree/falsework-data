package net.falsework.data.orm.entity;

import javax.persistence.MappedSuperclass;

import net.falsework.data.orm.mybatis.TypeAliases;

/**
 * 通用基类，实现了mybatis TypeAliases接口，可自动生成别名
 */
@MappedSuperclass
public class BaseEntity extends IdEntity implements TypeAliases{

	public BaseEntity() {
		super();
	}

	public BaseEntity(Long id) {
		super(id);
	}

	private static final long serialVersionUID = 1L;
	
	

}
