package net.falsework.data.orm.entity;


import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 统一定义id的entity基类.<br>
 * ID为Long型<br>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.<br>
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable{

	private static final long serialVersionUID = -205166679120870421L;

	@Id
	@GeneratedValue(generator = "longIdGenerator")
	@GenericGenerator(name = "longIdGenerator", strategy = "net.falsework.data.orm.entity.LongIdGenerator")
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdEntity other = (IdEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
