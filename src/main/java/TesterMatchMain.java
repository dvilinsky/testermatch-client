import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Main entry point for this program. Exlusively handles user input and displaying results to user
 */
public class TesterMatchMain {
    private static final String INPUT_ERROR_MSG = "Error, invalid input, please try again";
    private static Pattern inputRegex = Pattern.compile("^[\\w\\s\\d]+(?:,[\\w\\s]+)*$");

    public static void main(String[] args) throws URISyntaxException, IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            String countries = getField("country", input);
            checkQuit(countries);
            if (isValidInput(countries)){
                System.out.println(INPUT_ERROR_MSG);
                continue;
            }

            String devices = getField("device", input);
            checkQuit(devices);
            if (isValidInput(devices)){
                System.out.println(INPUT_ERROR_MSG);
                continue;
            }

            QueryExecutor executor = new QueryExecutor(Arrays.asList(countries.split(",")),
                    Arrays.asList(devices.split(",")));
            String response = executor.executeQuery();
            System.out.println("Here are the results of your query:");
            System.out.println(response);
        }
    }

    /**
     * This method provides a template for prompting for a search term
     * @param field The search dimension for which you want to prompt
     * @param input Scanner for reading user input
     * @return The user's input, stripped of whitespace
     */
    private static String getField(String field, Scanner input) {
        System.out.print("Enter a " + field + " or a comma-separated list of each " + field + ", or \"all\" for every " + field);
        System.out.println(" (enter to skip, \\q to quit)");
        System.out.println(field + ": ");
        return input.nextLine().strip();
    }

    /**
     * Checks to see if user input is "\q" and exits the program if so
     * @param input The user's input
     */
    private static void checkQuit(String input) {
        if (input.equals("\\q")) {
            System.exit(0);
        }
    }

    /**
     * Chekcs that the user input is a valid, comma-separated strng, or empty
     * @param s User input to be valid
     * @return True if valid, false otherwise
     */
    private static boolean isValidInput(String s) {
        return inputRegex.matcher(s).matches() || s.isEmpty();
    }
}
