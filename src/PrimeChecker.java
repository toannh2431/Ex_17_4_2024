
public class PrimeChecker {
	private String encodedAge;
    private boolean isPrime;

    public PrimeChecker(String encodedAge) {
        this.encodedAge = encodedAge;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public void run() {
        int sum = 0;
        for (char c : encodedAge.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        isPrime = isPrime(sum);
    }

    private boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}

