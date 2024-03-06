package org.eva.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class HttpHandlerTest {

    @Test
    public void testHandleRequestForIndexHtml() throws IOException {
        // Create a ByteArrayOutputStream to capture the server output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream, true);

        // Create an InputStream to simulate client input
        String request = "GET /index.html HTTP/1.1\n";
        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Create a mock Socket
        Socket mockSocket = Mockito.mock(Socket.class);
        Mockito.when(mockSocket.getInputStream()).thenReturn(inputStream);
        Mockito.when(mockSocket.getOutputStream()).thenReturn(outputStream);

        // Create HttpHandler instance and invoke run method
        HttpHandler httpHandler = new HttpHandler(mockSocket);
        httpHandler.run();

        // Verify the output
        String expectedResponse = """
                HTTP/1.1 200 OK
                Content-type:text/html

                """;
        StringBuilder expectedHtmlContent = new StringBuilder();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/index.html"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                expectedHtmlContent.append(line).append("\n");
            }
        }

        // Append expected HTML content to expected response
        expectedResponse += expectedHtmlContent.toString();

        assertEquals(expectedResponse, outputStream.toString());
        // Add assertions to verify HTML content if needed
    }
}