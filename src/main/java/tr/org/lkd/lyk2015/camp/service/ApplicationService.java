package tr.org.lkd.lyk2015.camp.service;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.dal.ApplicationDao;
import tr.org.lkd.lyk2015.camp.dal.CourseDao;
import tr.org.lkd.lyk2015.camp.dal.StudentDao;
import tr.org.lkd.lyk2015.camp.dto.ApplicationFormDto;
import tr.org.lkd.lyk2015.camp.model.Application;
import tr.org.lkd.lyk2015.camp.model.Course;
import tr.org.lkd.lyk2015.camp.model.Student;

@Transactional
@Service
public class ApplicationService extends GenericService<Application> {

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private ApplicationDao applicationDao;

	@Autowired
	private EmailService emailService;

	private static final String URL_BASE = "http://localhost:8080/camp/basvuru/validate/";

	public void create(ApplicationFormDto applicationFormDto) {

		Application application = applicationFormDto.getApplication();
		Student student = applicationFormDto.getStudent();
		List<Long> courseIds = applicationFormDto.getPreferredCourseIds();

		// Generate email verification url
		String uuid = UUID.randomUUID().toString();
		String url = URL_BASE + uuid;

		// Send verification email

		this.emailService.sendEmail(student.getEmail(), "Basvuru onayi", url);

		application.setValidationId(uuid);

		// Add preferred courses to application entity

		List<Course> courses = this.courseDao.getByIds(courseIds);
		application.getPreferredCourses().clear();
		application.getPreferredCourses().addAll(courses);

		// Check if user exists

		Student studentFormDb = this.studentDao.getUserByTckn(student.getTckn());

		if (studentFormDb == null) {

			this.studentDao.create(student);
			studentFormDb = student;
		}

		// Set application user

		application.setOwner(studentFormDb);
		this.emailService.sendEmail(studentFormDb.getEmail(), "başvuru onayı", url);
		this.applicationDao.create(application);

	}

	public boolean validate(String id) {
		Application application = this.applicationDao.getByValidationId(id);
		if (application == null) {
			return false;
		} else {
			application.setValidated(true);
			this.applicationDao.update(application);
			return true;
		}
	}

}
