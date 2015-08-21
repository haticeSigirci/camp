package tr.org.lkd.lyk2015.camp.service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tr.org.lkd.lyk2015.camp.dal.ApplicationDao;
import tr.org.lkd.lyk2015.camp.dal.CourseDao;
import tr.org.lkd.lyk2015.camp.dal.StudentDao;
import tr.org.lkd.lyk2015.camp.dto.ApplicationFormDto;
import tr.org.lkd.lyk2015.camp.model.Application;
import tr.org.lkd.lyk2015.camp.model.Course;
import tr.org.lkd.lyk2015.camp.model.Student;

@Transactional // Session acik burada
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

	private PasswordEncoder passwordEncoder;

	private static final String URL_BASE = "http://localhost:8080/camp/basvuru/validate/";

	public void create(ApplicationFormDto applicationFormDto) {

		Application application = applicationFormDto.getApplication();
		Student student = applicationFormDto.getStudent();
		List<Long> courseIds = applicationFormDto.getPreferredCourseIds();

		application.setYear(Calendar.getInstance().get(Calendar.YEAR));

		// Generate password for student to update his/her application later on
		String password = UUID.randomUUID().toString();
		password = password.substring(0, 6);

		// Generate email verification url
		String uuid = UUID.randomUUID().toString();
		String url = URL_BASE + uuid;

		String emailContent = "Dogrulamak icin tiklayiniz: " + url + "\nParolaniz: " + password;
		password = this.passwordEncoder.encode(password);

		// Send verification email

		this.emailService.sendEmail(student.getEmail(), "Basvuru onayi", emailContent);

		application.setValidationId(uuid);

		// Add preferred courses to application entity

		List<Course> courses = this.courseDao.getByIds(courseIds);
		application.getPreferredCourses().clear();
		application.getPreferredCourses().addAll(courses);

		// Check if user exists

		Student studentFormDb = this.studentDao.getUserByTckn(student.getTckn());

		if (studentFormDb == null) { // a new student, set password

			student.setPassword(password);
			this.studentDao.create(student);
			studentFormDb = student;
		} else { // existing student, update password
			// New
			studentFormDb.setPassword(password);
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

			// update gerek yok ?
			this.applicationDao.update(application);
			return true;
		}
	}

}
