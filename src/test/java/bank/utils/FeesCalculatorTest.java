package bank.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Use of Parameterized helps in this case, since multiple runs of same test are required
class FeesCalculatorTest {
	FeesCalculator calculator = new FeesCalculator();

	// Boundry values for withdrawal fee test
	// Student : Boolean
	// Weekend: Boolean
	// Balance between 0 - 1000
	// Balance between 1000 - 5000
	// Balance between 5000+


	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
    void withdrawalTest() {
        // Boundary values for the "Balance"
        int[] boundaryValues = {-1, 0, 1, 999, 1000, 1001, 4999, 5000, 5001};

        // Testing with all combinations of "Student" and "Weekend" booleans
        boolean[] studentStatuses = {true, false};
        boolean[] weekendStatuses = {true, false};
		

        // Iterate through all combinations
        for (boolean isStudent : studentStatuses) {
            for (boolean isWeekend : weekendStatuses) {
                for (int balance : boundaryValues) {
					int dayOfWeek = 0;

					if(isWeekend) {
						dayOfWeek = 1;// Sunday
					}
					else{
						dayOfWeek = 2;// Monday
	
					}

                    double expectedFee = calculateExpectedFee(isStudent, isWeekend, balance);
                    
                    // Perform the assertion
                    assertEquals(expectedFee, calculator.calculateWithdrawalFee(200,balance , isStudent, dayOfWeek), 
                        "Failed for Student: " + isStudent + ", Weekend: " + isWeekend + ", Balance: " + balance);
                }
            }
        }
    }

    // Helper method to determine expected fee based on the inputs
    // This will need to be updated based on your business logic
    private double calculateExpectedFee(boolean isStudent, boolean isWeekend, int balance) {
        // Example fee calculation logic - replace this with actual logic
        if(isStudent){
			if(isWeekend){
				return 0.1;
			}
			else{
				return 0.0;
			}
		}

		if(!isStudent){
			if(balance < 1000){
				return 0.3;
			}
			else if(balance > 1000 && balance < 5000){
				return 0.1;
			}
			else if(balance > 5000){
				return 0.0;
			}
		}
		return -1;
    }
}