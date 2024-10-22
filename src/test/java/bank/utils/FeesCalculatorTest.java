package bank.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;



// Use of Parameterized helps in this case, since multiple runs of same test are required
class FeesCalculatorTest {
	FeesCalculator calculator = new FeesCalculator();

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void withdrawalTest() {
		assertEquals(0.2, calculator.calculateWithdrawalFee(200, 1000, false, 0));		//pass
		//assertEquals(0.01, calculator.calculateWithdrawalFee(200, 1000, false, 0));	//fail
	}

	@ParameterizedTest
	@CsvSource({
			"25, 250, true, 0.0",
			"200, 1000, false, 20",
			"300, 3000, true, 150.0",
			"-10, 7000, false, -1",
			"25, 20000, true, 12.5",
			"200, -10, false, 20"
	})
	void depositTest(double amount, double accountBalance, boolean student, double expected) {
		assertEquals(expected, calculator.calculateDepositInterest(amount, accountBalance, student));
	}
}
