package tr.org.lkd.lyk2015.camp.service;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.model.Instructor;

import tr.org.lkd.lyk2015.camp.dal.InstructorDao;

@Service
@Transactional
public class InstructorService implements Serializable{

	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected InstructorDao InstructorDao;

	public Long create(final Instructor Instructor) {

		// creation Date update Date

		if (Instructor == null) {
			throw new RuntimeException("Model cannot be null");
		}

		return InstructorDao.create(Instructor);
	}

	public Instructor getById(final Long id) {

		if (id == null) {
			throw new RuntimeException("Id cannot be null");
		}

		return InstructorDao.getById(id);
	}

	public Instructor update(final Instructor Instructor) { // dao

		if (Instructor == null) {
			throw new RuntimeException("Model cannot be null");
		}

		return (Instructor) InstructorDao.update(Instructor);
	}

	public List<Instructor> getAll() {

		return InstructorDao.getAll();
	}

}
