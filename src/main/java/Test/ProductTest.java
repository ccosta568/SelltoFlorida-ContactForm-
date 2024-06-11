package Test;

import com.pwm.aws.crud.lambda.api.Server;
import com.sun.net.httpserver.HttpServer;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class ProductTest {
    private static HttpServer server;

    @BeforeAll
    public static void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/lambda", new Server.MyHandler());
        server.setExecutor(null);
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        server.stop(0);
    }

    @Test
    public void testHandleRequest() throws Exception {
        URL url = new URL("http://localhost:8000/lambda");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"httpMethod\":\"POST\", \"body\":\"{\\\"name\\\":\\\"John Doe\\\",\\\"email\\\":\\\"john.doe@example.com\\\",\\\"phone\\\":\\\"1234567890\\\",\\\"message\\\":\\\"Hello\\\"}\"}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        assertEquals(200, code);
    }

    @Test
    public void testInvalidHttpMethod() throws Exception {
        URL url = new URL("http://localhost:8000/lambda");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        String jsonInputString = "{\"httpMethod\":\"GET\"}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = connection.getResponseCode();
        assertEquals(405, code);
    }

    // Add more tests with different JSON payloads as needed
}
