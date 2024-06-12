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

//public class ProductTest
