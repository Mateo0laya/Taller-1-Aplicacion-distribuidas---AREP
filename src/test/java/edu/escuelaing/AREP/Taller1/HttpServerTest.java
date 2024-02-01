package edu.escuelaing.AREP.Taller1;

import static org.junit.Assert.*;

import org.junit.Test;


public class HttpServerTest {
    
    @Test 
    public void shouldReturnBasicInfo() {
        HttpServer httpServer = new HttpServer();

        String result = httpServer.searchMovie("The Matrix");

        assertNotNull(result);
        assertTrue(result.contains("Title"));
        assertTrue(result.contains("Director"));
        assertFalse(result.contains("Error")); 
    }

    @Test
    public void shouldReturnError() {
        HttpServer httpServer = new HttpServer();

        String result = httpServer.htttpError();
        assertNotNull(result);
        assertTrue(result.contains("400 Not Found"));
        assertTrue(result.contains("<title>Error Not Found</title>"));
    }

    @Test
    public void shouldReturnBasicHtml() {
        HttpServer httpServer = new HttpServer();

        String result = httpServer.httpClientHtml();

        assertNotNull(result);
        assertTrue(result.contains("Movies API"));
        assertTrue(result.contains("<form action=\"/hello\">"));
        assertTrue(result.contains("<style>"));
    }
}
