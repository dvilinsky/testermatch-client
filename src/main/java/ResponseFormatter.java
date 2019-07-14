import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * This class formats the results of a query executed on the backend. Since the results are essentially a table, the
 * output is formatted as such. The output will look like:
 * "First Name"   "Last Name"   "Experience"
 * "----------"   "---------"   "----------"
 * followed by the rows, if any, of data
 */
public class ResponseFormatter {
    private static final String FORMAT = "%-10s %-10s %-10s\n";

    private CloseableHttpResponse response;
    private ObjectMapper mapper;

    /**
     * Constructor for this class
     * @param response HTTP server response to be formatted
     */
    public ResponseFormatter(CloseableHttpResponse response) {
        this.response = response;
        this.mapper = new ObjectMapper();
    }

    /**
     * Does the heavy -lifting of formatting a response. Parses the input stream into a JSON String, leverages Jackson
     * to parse the JSON string into an array of data objects, and then builds up an output string
     * @return Formatted string of HTTP response
     * @throws IOException
     */
    public String format() throws IOException {
        String json = EntityUtils.toString(response.getEntity());
        TesterDTO[] testers = mapper.readValue(json, TesterDTO[].class);
        StringBuilder result = new StringBuilder();
        result.append(String.format(FORMAT, "First Name", "Last Name", "Experience"));
        result.append(String.format(FORMAT, "----------", "---------", "----------"));
        for (TesterDTO tester : testers) {
            result.append(String.format(FORMAT, tester.getFirstName(), tester.getLastName(), tester.getNumBugs()));
        }
        return result.toString();
    }
}