package tr.org.lkd.lyk2015.camp.dal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import tr.org.lkd.lyk2015.camp.model.Course;

@Repository
public class CourseDao extends GenericDao<Course> {

	public List<Course> getByIds(List<Long> ids) {

		Criteria criteria = this.createCriteria();
		criteria.add(Restrictions.in("id", ids));

		return criteria.list();

	}

	public List<Course> getAllActive() {
		Criteria c = this.createCriteria();
		c.add(Restrictions.eq("active", true));
		return c.list();
	}

	public Course getCourseWithInstructors(long id) {
		Criteria c = this.createCriteria();
		c.add(Restrictions.idEq(id));
		c.setFetchMode("instructors", FetchMode.JOIN);

		return (Course) c.uniqueResult();
	}
}
