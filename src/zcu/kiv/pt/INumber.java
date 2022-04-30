package zcu.kiv.pt;

/**
 * functional interface for testing strings
 */
public interface INumber {
    /**
     * method tests if input String has met specified conditions
     * @param input text input to be validated
     * @return true/false if test condition was/wasn't met
     */
    boolean test(String input);
}
