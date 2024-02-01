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
            String inputLine, outputLine, moviesName, okHeader;

            okHeader = "HTTP/1.1 200 OK\r\n"
            + "Content-Type:text/html\r\n"
            + "\r\n";

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
                outputLine = okHeader + outputLine;
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

    public static String searchMovie(String moviesName) {
        String outputLine = "";
        if(cache.containsKey(moviesName)){
            outputLine = cache.get(moviesName);
            System.out.println("Del caché");
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

    public static String htttpError() {
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
            "        <style>\n" +
            "           body {\n" +
            "               display: flex;\n" +
            "               flex-direction: column;\n" +
            "               align-items: center;\n" +
            "               justify-content: center;\n" +
            "               min-height: 100vh;\n" +
            "               margin: 0;\n" +
            "               background-color: #f0f8ff; /* Azul claro */\n" +
            "               font-family: Arial, sans-serif;\n" +
            "           }\n" +
            "           \n" +
            "           h1 {\n" +
            "               color: #333;\n" +
            "           }\n" +
            "           \n" +
            "           form {\n" +
            "               margin-top: 20px;\n" +
            "           }\n" +
            "           \n" +
            "           label {\n" +
            "               display: block;\n" +
            "               margin-bottom: 5px;\n" +
            "               color: #333;\n" +
            "           }\n" +
            "           \n" +
            "           input {\n" +
            "               padding: 8px;\n" +
            "               margin-bottom: 15px;\n" +
            "               border: 1px solid #999;\n" +
            "               border-radius: 4px;\n" +
            "           }\n" +
            "           \n" +
            "           input[type=\"button\"] {\n" +
            "               background-color: #007bff; /* Azul */\n" +
            "               color: #fff;\n" +
            "               cursor: pointer;\n" +
            "           }\n" +
            "           \n" +
            "           div#getrespmsg {\n" +
            "               margin-top: 20px;\n" +
            "           }\n" +
            "           \n" +
            "           table {\n" +
            "               border-collapse: collapse;\n" +
            "               width: 100%;\n" +
            "               margin-top: 15px;\n" +
            "           }\n" +
            "           \n" +
            "           table, th, td {\n" +
            "               border: 1px solid #ddd;\n" +
            "           }\n" +
            "           \n" +
            "           th, td {\n" +
            "               padding: 10px;\n" +
            "               text-align: left;\n" +
            "           }\n" +
            "           \n" +
            "           th {\n" +
            "               background-color: #007bff; /* Azul */\n" +
            "               color: #fff;\n" +
            "           }\n" +
            "        </style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <h1>Movie´s API</h1>\n" +
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
            "                    let response = document.getElementById(\"getrespmsg\");\n" +
            "                    let jsonData = JSON.parse(this.responseText);\n" +
            "                    let tableHtml = \"<table border='1'>\";\n" +
            "                    for (let key in jsonData) {\n" +
            "                        if (jsonData.hasOwnProperty(key)) {\n" +
            "                           if(key == \"Ratings\" && Array.isArray(jsonData[key])){\n" +
            "                               for (let i = 0; i < jsonData[key].length; i++) {\n" +
            "                                   tableHtml += \"<tr><td>\" + jsonData[key][i].Source + \"</td><td>\" + jsonData[key][i].Value + \"</td></tr>\";\n" +
            "                               }\n" +
            "                            } else {\n" +
            "                               tableHtml += \"<tr><td>\" + key + \"</td><td>\" + jsonData[key] + \"</td></tr>\";\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                    tableHtml += \"</table>\";\n" +
            "                    response.innerHTML = tableHtml;\n" +
            "                };\n" +
            "                xhttp.open(\"GET\",\"/hello?name=\"+nameVar);\n" +
            "                xhttp.send();\n" +
            "            }\n" +
            "        </script>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
        return outputLine;
    }
}