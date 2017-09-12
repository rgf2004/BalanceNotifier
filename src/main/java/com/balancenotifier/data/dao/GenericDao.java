package com.balancenotifier.data.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface GenericDao<T> extends Serializable
{
	
	 public T createEntity(T t);
	 
	 public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams);
	 
	 public List<T> findAll();
	
 //  public Query createQuery(String queryStr);
   
//   public void deleteEntity(Object id);
//
//   public T findEntity(Object id);
//
//   public Object getSingleResultObject(Query query);
//   
//   public Object getSingleResultObjectByNamedQuery(String queryName, Map<String, Object> queryParams);
//
//
//   public void deleteEntity(Long id);
//
//   public T findEntity(Long id);
//
//
//   public Long count();
//
//   public void flush();
//
//   public void clear();
//
//   public int deleteByNamedQuery(String queryName, Map<String, Object> queryParams);  
}
