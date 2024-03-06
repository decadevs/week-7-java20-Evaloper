package org.eva.util;

import java.io.*;
import java.net.Socket;

import static org.eva.common.HttpConstants.HTML_FILE_PATH;
import static org.eva.common.HttpConstants.JSON_FILE_PATH;

public class HttpHandler implements Runnable {
    private final Socket clientSocket;

    public HttpHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    @Override
    public void run() {
        try(BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))){
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = input.readLine();
            if (request != null){
                String[] parts = request.split("\\s+");
                if (parts.length >= 2 && parts[0].equals("GET")){
                    String path = parts[1];
                    switch (path.toLowerCase()){
                        case "/":
                        case  "/index.html":
                            sendNewHtmlResponse(output);
                            break;
                        case "/json":
                            sendNewJsonResponse(output);
                            break;
                        default:
                            sendNewNotFoundException(output);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNewHtmlResponse(PrintWriter output) throws IOException{
        File file = new File(HTML_FILE_PATH);
        sendResponse(output, file, "text/html");
    }

    private void sendNewJsonResponse(PrintWriter output) throws IOException {
        File file = new File(JSON_FILE_PATH);
        sendResponse(output, file, "application/json");
    }

    private void sendNewNotFoundException(PrintWriter output) {
        output.println("HTTP/1.1/ 404 Not found");
        output.println("Content-type: text/plain");
        output.println();
        output.println("404 Not Found - The requested resource was not found on this server");
    }

    private void sendResponse(PrintWriter output, File file, String contentType) throws IOException {
        try {
            if (file.exists()){
                output.println("HTTP/1.1 200 OK");
                output.println("Content-type:" + contentType);
                output.println();

                try(BufferedReader reader= new BufferedReader(new FileReader(file))){
                    String line;
                    while ((line = reader.readLine()) != null){
                        output.println(line);
//                        System.out.println(line);
                    }
                }
            } else {
                sendNewNotFoundException(output);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
