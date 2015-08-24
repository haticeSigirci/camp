package tr.org.lkd.lyk2015.camp.service;

import java.security.AccessControlException;
import java.util.ArrayList;
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

	@Autowired
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

			this.applicationDao.update(application);
			return true;
		}
	}

	public ApplicationFormDto createApplicationDto(Student student) {
		Application application = this.applicationDao.getStudentsApplication(student.getId());

		List<Long> courseIds = new ArrayList<>();
		for (Course course : application.getPreferredCourses()) {

			courseIds.add(course.getId());

		}

		int emptySize = 3 - courseIds.size();

		for (int i = 0; i < emptySize; i++) {
			courseIds.add(null);
		}

		Student studentFromDb = this.studentDao.getById(student.getId());

		ApplicationFormDto applicationFormDto = new ApplicationFormDto();
		applicationFormDto.setPreferredCourseIds(courseIds);
		applicationFormDto.setStudent(studentFromDb);
		applicationFormDto.setApplication(application);

		return applicationFormDto;
	}

	// update Application form
	public void update(ApplicationFormDto applicationFormDto) {

		Application application = applicationFormDto.getApplication();
		List<Long> courseIds = applicationFormDto.getPreferredCourseIds();

		// Add preferred courses to application entity
		List<Course> courses = this.courseDao.getByIds(courseIds);

		// Check if user exists

		// update each field

		Application applicationFromDb = this.applicationDao.getById(application.getId());
		applicationFromDb.setCorporation(application.getCorporation());
		applicationFromDb.setGithubLink(application.getGithubLink());
		applicationFromDb.setEnglishLevel(application.getEnglishLevel());
		applicationFromDb.setNeedAccomodation(application.isNeedAccomodation());
		applicationFromDb.setOfficer(application.isOfficer());
		applicationFromDb.setWorkDetails(application.getWorkDetails());
		applicationFromDb.setWorkStatus(application.getWorkStatus());

		application.getPreferredCourses().clear();
		application.getPreferredCourses().addAll(courses);

		// Set application user

		// this.applicationDao.create(application);

	}

	public void isUserAuthorizedForForm(Student student, Application application) {

		Application applicationFromDb = this.applicationDao.getStudentsApplication(student.getId());

		if (applicationFromDb == null || !applicationFromDb.getId().equals(application.getId())) {
			throw new AccessControlException("This form is not owned by current user");
		}

	}

}
