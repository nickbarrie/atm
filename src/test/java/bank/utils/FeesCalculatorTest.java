package bank.utils;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.CsvSource;

// Use of Parameterized helps in this case, since multiple runs of same test are required
class FeesCalculatorTest {
	FeesCalculator calculator = new FeesCalculator();

	// Boundry values for withdrawal fee test
	// Student : Boolean
	// Weekend: Boolean
	// Balance between 0 - 1000
	// Balance between 1000 - 5000
	// Balance between 5000+

	// Boundary values for withdrawal fee test
	int[] boundaryValues = {999, 1000, 1001, 4999, 5000, 5001};
	boolean[] studentStatuses = {true, false};
	boolean[] weekendStatuses = {true, false};

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

   // Dynamic test factory method
    @TestFactory
    Collection<DynamicTest> withdrawalTestAssignmentOne() {
        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        for (boolean isStudent : studentStatuses) {
            for (boolean isWeekend : weekendStatuses) {
                for (int balance : boundaryValues) {

                    String testName = "Student: " + isStudent + ", Weekend: " + isWeekend + ", Balance: " + balance;

                    DynamicTest dynamicTest = DynamicTest.dynamicTest(testName, () -> {
                        int dayOfWeek = isWeekend ? 1 : 2; // Sunday or Monday

                        double expectedFee = calculateExpectedFee(isStudent, isWeekend, balance);

                        assertEquals(expectedFee, calculator.calculateWithdrawalFee(200, balance, isStudent, dayOfWeek), 
                            "Failed for Student: " + isStudent + ", Weekend: " + isWeekend + ", Balance: " + balance);
                    });

                    // Add dynamic test case to collection
                    dynamicTests.add(dynamicTest);
                }
            }
        }

        return dynamicTests;
    }

    private double calculateExpectedFee(boolean isStudent, boolean isWeekend, int balance) {
		if(balance <= 0){
			return 0;
		}
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
			else if(balance >= 1000 && balance <= 5000){
				return 0.1;
			}
			else if(balance > 5000){
				return 0.0;
			}
		}
		return -1;
    }

	// Withdrawal fee test for assignment 2
	@ParameterizedTest
	@CsvSource({//6 is Friday and 7 is Saturday
			"1, 999, true, 7, 0.0",
			"1, 999, true, 6, 0.001",
			"1, 999, false, 7, 0.002",
			"1, 1000, false, 7, 0.001",
			"1, 10001, false, 7, 0.0"
	})
	void withdrawalTestAssignmentTwo(double amount, double accountBalance, boolean student, int dayOfWeek, double expected) {
		assertEquals(expected, calculator.calculateWithdrawalFee(amount, accountBalance, student, dayOfWeek));
	}


	@ParameterizedTest
	@CsvFileSource(resources = "/TransferFeesTestCases.csv", numLinesToSkip = 1) // Skip header if necessary
	void testMyMethodFromCsvFile(boolean student, double amount, double fromAccountBalance, double toAccountBalance, double expectedResult){
		FeesCalculator calculator = new FeesCalculator();
		double result = calculator.calculateTransferFee(amount, fromAccountBalance, toAccountBalance, student);
		assertEquals(expectedResult, result);
	}
  
	@ParameterizedTest
	@CsvSource({
			"25, 250, true, 0.0",
			"200, 1000, false, 0.2",
			"300, 3000, true, 1.5",
			"-10, 7000, false, -0.01",
			"25, 20000, true, 0.125",
			"200, -10, false, 0.2"
	})
	void depositTest(double amount, double accountBalance, boolean student, double expected) {
		assertEquals(expected, calculator.calculateDepositInterest(amount, accountBalance, student));
	}
}
