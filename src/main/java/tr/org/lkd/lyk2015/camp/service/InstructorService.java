package tr.org.lkd.lyk2015.camp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.dal.CourseDao;
import tr.org.lkd.lyk2015.camp.dal.InstructorDao;
import tr.org.lkd.lyk2015.camp.model.Course;
import tr.org.lkd.lyk2015.camp.model.Instructor;

@Service
@Transactional
public class InstructorService extends GenericService<Instructor>{
	

	@Autowired
	private InstructorDao instructorDao;
										
	@Autowired
	private CourseDao courseDao;
	
	public void create(Instructor instructor, List<Long> ids) {
			
			List<Course> courses = courseDao.getByIds(ids);
			
			instructor.getCourses().addAll(courses);
			
		
	
			instructorDao.create(instructor);
		}
	
	//Lazy loading icin cozum
	
	public Instructor getInstructorWithCourses(Long id) {
		
		Instructor instructor = instructorDao.getByIdWithCourse(id);
//		Hibernate.initialize(instructor.get);
		
		return instructor;
	}

	//butun courselarin yazili gelmesi ait olanlarin secili gelmesi
	

}
