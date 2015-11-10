package net.falsework.data.orm.entity;

import java.io.Serializable;

/**
 * 主键生成器
 * @param <ID>
 */
public interface IdGenerator<ID extends Serializable> {

	public ID generate();
}
