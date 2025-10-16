package me.duanbn.snailfish.core.orm.rdb;

import java.util.List;

import me.duanbn.snailfish.core.domain.pattern.DomainSPEC;

@SuppressWarnings({ "rawtypes" })
public interface DataObjectDao<E, PK extends Number> {

	PK insert(E entity);

	PK insert(E entity, boolean autoIncrementId, boolean includeSuperClass);

	int insertBatch(List<E> entityList);

	int insertBatch(List<E> entityList, boolean autoIncrementId, boolean includeSuperClass);

	int update(E entity);

	int update(E entity, boolean includeSuperClass);

	int[] updateBatch(List<E> entityList);

	int[] updateBatch(List<E> entityList, boolean includeSuperClass);

	void deleteByIds(List<PK> ids);

	void deleteByQuery(Query query);

	boolean exist(PK id);

	Query createQuery(DomainSPEC spec);

	Query createQuery();

	E findById(PK id);

	List<E> findByIds(List<PK> ids);

	long count();

	long countByQuery(Query query);

	E findOneByQuery(Query query);

	List<E> findAll();

	List<E> findByQuery(Query query);

	/**
	 * real delete all record ref by this dao. <b>be careful use it.</b>
	 */
	void clean();

	Class getDataObjectClass();

}
