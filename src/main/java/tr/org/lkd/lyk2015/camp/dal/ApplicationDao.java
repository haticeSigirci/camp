package tr.org.lkd.lyk2015.camp.dal;

import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import tr.org.lkd.lyk2015.camp.model.Application;

@Repository
public class ApplicationDao extends GenericDao<Application> {

	public Application getByValidationId(String id) {
		Criteria criteria = this.createCriteria();
		criteria.add(Restrictions.eq("validationId", id));
		return (Application) criteria.uniqueResult();
	}

	public Application getStudentsApplication(Long studentId) {
		Criteria criteria = this.createCriteria();

		criteria.createAlias("owner", "o");
		criteria.add(Restrictions.eq("o.id", studentId));
		criteria.add(Restrictions.eq("year", Calendar.getInstance().get(Calendar.YEAR)));

		criteria.setFetchMode("preferredCourses", FetchMode.JOIN); // prevent
																	// lazy
																	// loading

		return (Application) criteria.uniqueResult();
	}

	// method chaining
}
