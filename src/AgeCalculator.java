
public class AgeCalculator {
	 private Student student;
	    private String encodedAge;

	    public AgeCalculator(Student student) {
	        this.student =  new Student(encodedAge, encodedAge, encodedAge, encodedAge);
	    }

	    public String getEncodedAge() {
	        return encodedAge;
	    }

	    public void run() {
	        String[] dobParts = student.getDateOfBirth().split("-");
	        int year = Integer.parseInt(dobParts[0]);
	        int age = 2024 - year;
	        encodedAge = encodeNumber(age);
	    }

	    private String encodeNumber(int number) {
	        return String.valueOf(number);
	    }
	}


