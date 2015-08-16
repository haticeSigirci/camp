package tr.org.lkd.lyk2015.camp.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.dal.AdminDao;
import tr.org.lkd.lyk2015.camp.model.Admin;


@Service
@Transactional
public class AdminService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected AdminDao adminDao;
	
	public Long create(final Admin admin) {
		
		//creation Date update Date

		if (admin == null) {
			throw new RuntimeException("Model cannot be null");
		}

		return adminDao.create(admin);
	}
	
	public Admin getById(final Long id) {

		if (id == null) {
			throw new RuntimeException("Id cannot be null");
		}

		return adminDao.getById(id);
	}
	
	public Admin update(final Admin admin) { //dao

		if (admin == null) {
			throw new RuntimeException("Model cannot be null");
		}

		return (Admin) adminDao.update(admin);
	}
	
	public List<Admin> getAll() {

		return adminDao.getAll();
	}
	
}
