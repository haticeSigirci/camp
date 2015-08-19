package tr.org.lkd.lyk2015.camp.service;

public interface EmailService {

	// public abstract boolean validate(String email);

	public void sendEmail(String to, String subject, String content);
}
