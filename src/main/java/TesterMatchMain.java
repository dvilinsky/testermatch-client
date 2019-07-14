import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;

public class TesterMatchMain {
    private static final String INPUT_ERROR_MSG = "Error, invalid input, please try again";

    public static void main(String[] args) throws URISyntaxException, IOException {
        Scanner input = new Scanner(System.in);
        while (true) {
            String countries = getField("country", input);
            checkQuit(countries);
            if (!Validators.isValidInput(countries)){
                System.out.println(INPUT_ERROR_MSG);
                continue;
            }

            String devices = getField("device", input);
            checkQuit(devices);
            if (!Validators.isValidInput(devices)){
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

    private static String getField(String field, Scanner input) {
        System.out.print("Enter a " + field + " or a comma-separated list of each " + field + ", or \"all\" for every " + field);
        System.out.println(" (enter to skip, \\q to quit)");
        System.out.println(field + ": ");
        return input.nextLine().strip();
    }

    private static void checkQuit(String input) {
        if (input.equals("\\q")) {
            System.exit(0);
        }
    }
}
