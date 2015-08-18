package tr.org.lkd.lyk2015.camp.service;

import org.springframework.stereotype.Service;

@Service
public class MockTcknValidationService implements TcknValidationService {

	@Override
	public boolean validate(String name, String surname, Integer birthDate, Long tckn) {
		// TODO Auto-generated method stub

		if (tckn.equals(111L)) {
			return false;
		}
		return true;
	}

}
