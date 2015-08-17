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
public class InstructorService extends GenericService<Instructor>{
	

	@Autowired
	protected InstructorDao InstructorDao;


}
