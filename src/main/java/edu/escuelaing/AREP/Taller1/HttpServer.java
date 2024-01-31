package edu.escuelaing.AREP.Taller1;

import java.net.*;
import java.util.HashMap;
import java.io.*;

public class HttpServer {

    private static HashMap<String, String> cache = new HashMap<String, String>();
    private static HttpConnection httpConnection = new HttpConnection();
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;

        int port = 35000;
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port + ".");
            System.exit(1);
        }
        
        Socket clientSocket = null;
        
        boolean running = true;
        
        while (running) {

            try {
                System.out.println("Ready to recive ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
    
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine, moviesName;

            boolean firstLine = true;
            String uriStr = "";

            while ((inputLine = in.readLine()) != null) {
                if(firstLine){
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            if(uriStr.startsWith("/hello")){
                moviesName = uriStr.split("=")[1];
                outputLine = searchMovie(moviesName);
            }else{
                outputLine = httpClientHtml();
            }
            
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }

        serverSocket.close();

        
    
    }

    private static String searchMovie(String moviesName) {
        String outputLine = "";
        if(cache.containsKey(moviesName)){
            outputLine = cache.get(moviesName);
        }else{
            try {
                outputLine = httpConnection.query(moviesName);
                cache.put(moviesName, outputLine);
            } catch (IOException e) {
                outputLine = htttpError();
                System.out.println("Error: " + e.getMessage());
            }
        }
        return outputLine;
    }

    private static String htttpError() {
        String outputLine =  "HTTP/1.1 400 Not Found\r\n"
        + "Content-Type:text/html\r\n"
        + "\r\n"
        + "<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Error Not Found</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>Error</h1>\n" +
            "    </body>\n";
        return outputLine;
    }

    public static String httpClientHtml() {
        String outputLine = "HTTP/1.1 200 OK\r\n"
        + "Content-Type:text/html\r\n"
        + "\r\n"
        +"<!DOCTYPE html>\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <title>Movies API</title>\n" +
            "        <meta charset=\"UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>Form with GET</h1>\n" +
            "        <form action=\"/hello\">\n" +
            "            <label for=\"name\">Name:</label><br>\n" +
            "            <input type=\"text\" id=\"name\" name=\"name\" placeholder=\"Movie´s Name\"><br><br>\n" +
            "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
            "        </form>\n" +
            "        <div id=\"getrespmsg\"></div>\n" +
            "\n" +
            "    <script>\n" +
            "            function loadGetMsg() {\n" +
            "                let nameVar = document.getElementById(\"name\").value;\n" +
            "                const xhttp = new XMLHttpRequest();\n" +
            "                xhttp.onload = function() {\n" +
            "                    document.getElementById(\"getrespmsg\").innerHTML = this.responseText;\n" +
            "                };\n" +
            "                xhttp.open(\"GET\",\"/hello?name=\"+nameVar);\n" +
            "                xhttp.send();\n" +
            "            }\n" +
            "        </script>\n" +
            "\n" +
            "    <h1>Form with POST</h1>\n" +
            "    <form action=\"/hellopost\">\n" +
            "        <label for=\"postname\">Name:</label><br>\n" +
            "        <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n" +
            "        <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n" +
            "    </form>\n" +
            "\n" +
            "    <div id=\"postrespmsg\"></div>\n" +
            "\n" +
            "    <script>\n" +
            "        function loadPostMsg(name) {\n" +
            "            let url = \"/hello?name=\" + name.value;\n" +
            "\n" +
            "            fetch(url, {method: 'POST'})\n" +
            "                .then(x => x.text())\n" +
            "                .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n" +
            "        }\n" +
            "    </script>\n" +
            "</body>\n" +
            "</html>";
        return outputLine;
    }
}