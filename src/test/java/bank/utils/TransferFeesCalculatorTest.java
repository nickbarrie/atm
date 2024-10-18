package bank.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import static org.junit.jupiter.api.Assertions.*;

// Owen's tests to ensure fees for Transferring money are correct
// Use of Parameterized helps in this case, since multiple runs of same test are required
class TransferFeesCalculatorTest {

	//FeesCalculator method to test. Return a double
	//double calculateTransferFee(double amount, double fromAccountBalance, double toAccountBalance, boolean student)

	@ParameterizedTest
	@CsvFileSource(resources = "/TransferFeesTestCases.csv", numLinesToSkip = 1) // Skip header if necessary
	void testMyMethodFromCsvFile(boolean student, double amount, double fromAccountBalance, double toAccountBalance, double expectedResult){
		FeesCalculator calculator = new FeesCalculator();
		double result = calculator.calculateTransferFee(amount, fromAccountBalance, toAccountBalance, student);
		assertEquals(expectedResult, result);
	}

}