import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ResponseFormatter {
    private static final String FORMAT = "%-10s %-10s %-10s\n";

    private CloseableHttpResponse response;
    private ObjectMapper mapper;

    public ResponseFormatter(CloseableHttpResponse response) {
        this.response = response;
        this.mapper = new ObjectMapper();
    }

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