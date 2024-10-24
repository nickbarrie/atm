package atm.session;

import static org.mockito.Mockito.*;

import java.util.stream.Stream;

import atm.ATM;
import atm.dispatcher.MessageDispatcher;
import atm.session.Session;
import atm.session.states.SessionState;
import atm.ui.panels.MainPanel;
import atm.utils.CredentialsCheck;
import atm.utils.FormatChecker;
import atm.exceptions.InvalidAmountException;
import atm.exceptions.InvalidPinFormatException;
import atm.ui.panels.MainPanel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import atm.dispatcher.MessageDispatcher;
import atm.ui.panels.MainPanel;
import atm.session.Session;
import atm.session.transactions.ATMTransaction;
import bank.transactions.utils.AccountType;
import bank.transactions.utils.TransactionResult;
import bank.transactions.utils.TransactionType;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


public class AtmSessionTest {

    private Session session;
    private MainPanel mockMainPanel;
    private MessageDispatcher mockDispatcher;
    private ATMTransaction Withdrawal;
    private TransactionResult mockTransactionResult;
    
    @BeforeEach
    public void setUp() {
        // mock objects for dependencies
        mockMainPanel = Mockito.mock(MainPanel.class);
        mockDispatcher = Mockito.mock(MessageDispatcher.class);
        Withdrawal = Mockito.mock(ATMTransaction.class);

        // initialize the Session object with mock dependencies
        session = new Session(mockMainPanel, mockDispatcher);
    }

    
    // test cases for both valid and invalid pins
    private static Stream<Arguments> providePinsForTesting() {
        return Stream.of(
            Arguments.of(new char[] {'5', '4', '3', '2', '1'}, true),  // valid PIN
            Arguments.of(new char[] {'5', '4', '3', '2'}, false),      // short PIN
            Arguments.of(new char[] {'5', '4', '3', '2', '1', '0'}, false)  // long PIN
        );
    }
    
    // tests for both valid and invalid PINs
    @ParameterizedTest
    @MethodSource("providePinsForTesting")
    public void testPinFormat(char[] pin, boolean isValid) {
        if (isValid) {
            // valid PIN should not throw an exception
            assertDoesNotThrow(() -> session.addPin(pin));
        } else {
            // invalid PIN should throw InvalidPinFormatException
            assertThrows(InvalidPinFormatException.class, () -> {
                session.addPin(pin);
            });
        }
    }

    // test cases for withdrawals
    private static Stream<Arguments> provideAmountsForWithdrawal() {
        return Stream.of(
            Arguments.of(1001, true),  // invalid
            Arguments.of(1000, false), // valid
            Arguments.of(500, false),  // valid
            Arguments.of(0, false),    // valid
            Arguments.of(-1, true)     // invalid
        );
    }
    
    // parameterized test cases for different withdrawal amounts
    @ParameterizedTest
    @MethodSource("provideAmountsForWithdrawal")
    public void testWithdraw(int amount, boolean shouldThrowException) throws InvalidAmountException {
        session.setTransaction(Withdrawal);

        // changing the mock transaction type to withdrawal
        Mockito.when(Withdrawal.getTransactionType()).thenReturn(TransactionType.Withdrawal);

        if (shouldThrowException) {
            // should throw exception for invalid amounts
            assertThrows(InvalidAmountException.class, () -> {
                session.setAmount(amount);
            });
        } else {
            // valid should not throw exception
            assertDoesNotThrow(() -> session.setAmount(amount));
        }
    }

}
