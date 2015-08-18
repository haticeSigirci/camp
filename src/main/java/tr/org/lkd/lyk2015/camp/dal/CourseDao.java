package tr.org.lkd.lyk2015.camp.dal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import tr.org.lkd.lyk2015.camp.model.Course;

@Repository
public class CourseDao extends GenericDao<Course> {

	public List<Course> getByIds(List <Long> ids) {

		Criteria criteria = createCriteria();
		criteria.add(Restrictions.in("id", ids));
		
		
		return criteria.list();

	}
}
