package dataAccess;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.NamedQuery;

public class RepositoryBase<T> {

	private EntityManagerFactory Context;
	
	private Class<T> Type;
	
	private String Query;
	
	private EntityManager Manager;
	
	public RepositoryBase(Class<T> _Type)
	{
		Context = Persistence.createEntityManagerFactory("DataAccess");
		Manager = Context.createEntityManager();
		Type = _Type;
		
	    NamedQuery q = Type.getAnnotation(NamedQuery.class);
	    
	    Query = q.query();
	}
	
	/**
	 * Petici�n para obtener todos las entidades del tipo T
	 * @return 
	 */
	public List<T> GetAll()
	{
		Manager.getTransaction().begin();
		
		List<T> result = Manager.createQuery(Query, Type).getResultList();
		
		Manager.getTransaction().commit();
		
		return result;
	}
	
	/**
	 * Busca una entidad por su identificador
	 * @param Id
	 * @return
	 */
	public T FindById(int Id)
	{
		Manager.getTransaction().begin();
		
		T result = Manager.find(Type, Id);
		
		Manager.getTransaction().commit();
		
		return result;
	}
	
	/**
	 * Actualiza una entidad con identificador Id en base a updEntity 
	 * @param Id
	 * @param updEntity
	 * @return
	 */
	public T Update(int Id, T updEntity)
	{
		Manager.getTransaction().begin();
		
		T entity = Manager.find(Type, Id);
		
		Manager.detach(updEntity);
		
		Manager.merge(entity);
		
		Manager.getTransaction().commit();
		
		return entity;
	}
	
	/**
	 * Agrega una nueva entidad
	 * @param entity
	 * @return
	 */
	public T Add(T entity)
	{
		Manager.getTransaction().begin();
		
		Manager.persist(entity);
		
		Manager.getTransaction().commit();
		
		return entity;
	}
	
	/**
	 * Cierre de la conexi�n
	 */
	public void Dispose()
	{
		if (Manager != null) Manager.close();
	}
}
