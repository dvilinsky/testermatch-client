import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This class does the bulk of the work of the program. It routes each search query to the correct endpoint, builds
 * up an HTTP request, executes the request, and returns the result to the client in a prettified format.
 */
public class QueryExecutor {
    private static final int SERVER_PORT = 8080;
    private static final String ALL_TESTERS_PATH = "testers/all";
    private static final String TESTER_BASE_PATH = "testers";

    private List<String> countries;
    private List<String> devices;
    private CloseableHttpClient client;

    /**
     * Constructor for this class.
     * @param countries List of countries for which you wish to search for testers in. Can be empty
     * @param devices List of devices for which you wish to search for testers testing on. Can be empty;
     */
    public QueryExecutor(List<String> countries, List<String> devices) {
        this.countries = countries;
        this.devices = devices;
        this.client = HttpClientBuilder.create().build();
    }

    /**
     * Does the main work of routing and executes the query
     * @return The result set of each query
     * @throws URISyntaxException
     * @throws IOException
     */
    public String executeQuery() throws URISyntaxException, IOException {
        String country0 = countries.get(0);
        String device0 = devices.get(0);
        URI uri = null;
        //When either country0 or device0 are empty or all, that means the end user has not chosen to search for
        //testers by country or device
        if ((country0.isEmpty() || country0.equals("all")) && (device0.isEmpty() || device0.equals("all"))) {
            uri = getUri();
        } else if (country0.isEmpty() || country0.equals("all")) {
            uri = getUri("devices", devices); //It is a little bit weird that I am passing a class variable to a method of that class...
                                        //The other option is perhaps having four different methods, maybe getUri(), getURIAll() etc
        } else if (device0.isEmpty() || device0.equals("all")) {
            uri = getUri("countries", countries);
        } else {
            uri = getUri(devices, countries);
        }
        HttpGet get = new HttpGet(uri);
        CloseableHttpResponse response = client.execute(get);
        return new ResponseFormatter(response).format();
    }

    /**
     * Generates the URI for the "find all testers" case
     * @return Correct URI for this search
     * @throws URISyntaxException
     */
    private URI getUri() throws URISyntaxException {
        URIBuilder builder = getBaseBuilder(ALL_TESTERS_PATH);
        return builder.build();
    }

    /**
     * Generates the URI for a search done either by country, or by device, but not both
     * @param param Search key
     * @param params Values for which you wish to search
     * @return Correct URI for this search
     * @throws URISyntaxException
     */
    private URI getUri(String param, List<String> params) throws URISyntaxException {
        URIBuilder builder = getBaseBuilder(TESTER_BASE_PATH + "/" + param);
        for (String paramValue : params) {
            builder.addParameter(param, paramValue);
        }
        return builder.build();
    }

    /**
     * Generates the URI for a search by both country and device.
     * @param devices List of devices on which search is to be performed
     * @param countries List of countries on which search is to be performed
     * @return Correct URI for this search
     * @throws URISyntaxException
     */
    private URI getUri(List<String> devices, List<String> countries) throws URISyntaxException {
        URIBuilder builder = getBaseBuilder(TESTER_BASE_PATH);
        for (String country : countries) {
            builder.addParameter("countries", country);
        }
        for (String device : devices) {
            builder.addParameter("devices", device);
        }
        return builder.build();
    }

    private URIBuilder getBaseBuilder(String path) {
        return new URIBuilder().setScheme("http").setHost("localhost").setPort(SERVER_PORT).setPath(path);
    }
}

//                System.out.println(EntityUtils.toString(response.getEntity()));