package com.balancenotifier.data.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class JPADao<T> implements GenericDao<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1624064411648787311L;

	@Autowired
	private SessionFactory sessionFactory;

	private Class<T> type;

	@SuppressWarnings("unchecked")
	public JPADao() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		type = (Class) pt.getActualTypeArguments()[0];
	}

	/**
	 * @param t
	 * @return t
	 */
	public T createEntity(final T t) {
		this.sessionFactory.getCurrentSession().persist(t);
		return t;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
		Query namedQuery = this.sessionFactory.getCurrentSession().getNamedQuery(queryName);
		
		if (queryParams != null) {
			for (String s : queryParams.keySet()) {
				namedQuery.setParameter(s, queryParams.get(s));
			}
		}

		return namedQuery.list();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {

		Query query = this.sessionFactory.getCurrentSession().createQuery("from " + type.getSimpleName());
		return query.list();
		
	}


//	public void deleteEntity(final Object id) {
//		this.sessionFactory.getCurrentSession().remove(this.sessionFactory.getCurrentSession().getReference(type, id));
//	}
//
//	public void deleteEntity(final Long id) {
//		this.sessionFactory.getCurrentSession().remove(this.sessionFactory.getCurrentSession().getReference(type, id));
//	}
//
//	public T findEntity(final Object id) {
//		return (T) this.sessionFactory.getCurrentSession().find(type, id);
//	}
//
//	public T findEntity(final Long id) {
//		return (T) this.sessionFactory.getCurrentSession().find(type, id);
//	}
//
//	public T updateEntity(final T t) {
//		return this.sessionFactory.getCurrentSession().merge(t);
//	}
//
//	@SuppressWarnings("rawtypes")
//	public Object getSingleResultObject(Query query) {
//		List list = query.getResultList();
//		if (!CollectionUtils.isEmpty(list)) {
//			return list.get(0);
//		}
//		return null;
//	}
//
//	public Object getSingleResultObjectByNamedQuery(String queryName, Map<String, Object> queryParams) {
//		Query namedQuery = this.sessionFactory.getCurrentSession().createNamedQuery(queryName);
//
//		if (queryParams != null) {
//			for (String s : queryParams.keySet()) {
//				namedQuery.setParameter(s, queryParams.get(s));
//			}
//		}
//
//		List list = namedQuery.getResultList();
//
//		if (!CollectionUtils.isEmpty(list)) {
//			return list.get(0);
//		}
//		return null;
//	}
//
//	public Query createQuery(String queryStr) {
//		return sessionFactory.getCurrentSession().createQuery(queryStr, type);
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
//		Query namedQuery = em.createNamedQuery(queryName);
//
//		if (queryParams != null) {
//			for (String s : queryParams.keySet()) {
//				namedQuery.setParameter(s, queryParams.get(s));
//			}
//		}
//
//		return namedQuery.getResultList();
//	}
//
//	@SuppressWarnings("unchecked")
//	public int deleteByNamedQuery(String queryName, Map<String, Object> queryParams) {
//		Query namedQuery = em.createNamedQuery(queryName);
//
//		if (queryParams != null) {
//			for (String s : queryParams.keySet()) {
//				namedQuery.setParameter(s, queryParams.get(s));
//			}
//		}
//
//		return namedQuery.executeUpdate();
//	}
//
//	public List<T> findAll() {
//		Query query = em.createQuery("from " + type.getSimpleName());
//		return query.getResultList();
//	}
//
//	public Long count() {
//		Query query = em.createQuery("select count(o) from " + type.getSimpleName() + " o ");
//		Long count = (Long) query.getSingleResult();
//
//		return count;
//	}

}
