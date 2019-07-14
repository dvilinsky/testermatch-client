import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class QueryExecutor {
    private static final int SERVER_PORT = 8080;
    private static final String ALL_TESTERS_PATH = "testers/all";
    private static final String TESTER_BASE_PATH = "testers";

    private List<String> countries;
    private List<String> devices;
    private CloseableHttpClient client;

    public QueryExecutor(List<String> countries, List<String> devices) {
        this.countries = countries;
        this.devices = devices;
        this.client = HttpClientBuilder.create().build();
    }

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

    private URI getUri() throws URISyntaxException {
        URIBuilder builder = getBaseBuilder(ALL_TESTERS_PATH);
        return builder.build();
    }

    private URI getUri(String param, List<String> params) throws URISyntaxException {
        URIBuilder builder = getBaseBuilder(TESTER_BASE_PATH + "/" + param);
        for (String paramValue : params) {
            builder.addParameter(param, paramValue);
        }
        return builder.build();
    }

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