package tr.org.lkd.lyk2015.camp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.dal.CourseDao;
import tr.org.lkd.lyk2015.camp.model.Course;

@Service
@Transactional
public class CourseService extends GenericService<Course> {

	@Autowired
	protected CourseDao courseDao;

	public List<Course> getAllActive() {
		return this.courseDao.getAllActive();
	}

	public Course getInstructorsOfCourse(long id) {
		return this.courseDao.getCourseWithInstructors(id);
	}

}
