import java.util.regex.Pattern;

public class Validators {
    private static Pattern inputRegex = Pattern.compile("^[\\w\\s\\d]+(?:,[\\w\\s]+)*$");

    public static boolean isValidInput(String s) {
        return inputRegex.matcher(s).matches() || s.isEmpty();
    }
}
