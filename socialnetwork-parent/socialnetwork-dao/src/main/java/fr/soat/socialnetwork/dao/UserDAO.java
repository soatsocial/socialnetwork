package fr.soat.socialnetwork.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.myfaces.extensions.cdi.jpa.api.Transactional;

import fr.soat.socialnetwork.dao.entity.UserDTO;

public class UserDAO implements IUserDAO {

	@PersistenceContext(unitName = "soatsocial")
	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.soat.socialnetwork.dao.IUserDAO#find(long)
	 */
	@Transactional
	public UserDTO find(long id) {
		return em.find(UserDTO.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.soat.socialnetwork.dao.IUserDAO#save(fr.soat.socialnetwork.dao.User)
	 */
	@Transactional
	public UserDTO save(UserDTO entity) {
		em.persist(entity);
		return em.merge(entity);
	}

	public UserDTO getByEmail(String email) {
		Query q = em.createQuery("SELECT u FROM user u");
		List<UserDTO> results = (List<UserDTO>) q.getResultList();
		return results.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.soat.socialnetwork.dao.IUserDAO#update(fr.soat.socialnetwork.dao.User)
	 */
	@Transactional
	public UserDTO update(UserDTO entity) {
		em.persist(entity);
		return em.merge(entity);
	}
}