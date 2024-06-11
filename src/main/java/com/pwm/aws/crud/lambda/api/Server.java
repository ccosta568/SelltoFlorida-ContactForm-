package com.pwm.aws.crud.lambda.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/lambda", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    public static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            InputStream inputStream = t.getRequestBody();
            OutputStream outputStream = t.getResponseBody();

            ProductLambdaHandler handler = new ProductLambdaHandler();
            handler.handleRequest(inputStream, outputStream, null);

            outputStream.close();
        }
    }
}
