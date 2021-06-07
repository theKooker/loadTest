package src.dispatch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Request {
    private String baseUrl;
    private HttpURLConnection connection;
    private String body;

    Request(String url) {

        this.baseUrl = url;
        URL reUrl = null;
        try {
            reUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            this.connection = (HttpURLConnection) reUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connection.setRequestProperty("Accept", "Application/json");
        this.connection.setRequestProperty("Authorization", "Bearer HjqY4wFEQ5rCKwfgxhhSU2znJrEOYfOOSdfR");
    }
    Request(String url,int request) {

        this.baseUrl = url;
        URL reUrl = null;
        try {
            reUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            this.connection = (HttpURLConnection) reUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connection.setRequestProperty("Accept", "Application/json");
    }
    Request(String url, String body) {
        this.baseUrl = url;
        this.body = body;
        URL reUrl = null;
        try {
            reUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            this.connection = (HttpURLConnection) reUrl.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connection.setRequestProperty("Content-Type", "application/json; utf-8");
        this.connection.setRequestProperty("Authorization", "Bearer HjqY4wFEQ5rCKwfgxhhSU2znJrEOYfOOSdfR");
    }

    String getInput() throws IOException {
        // To store our response
        StringBuilder content;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = input.readLine()) != null) {
                // Append each line of the response and separate them
                content.append(line);
                content.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        return content.toString();
    }
    Address getAddress() throws IOException{
        StringBuilder content;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = input.readLine()) != null) {
                // Append each line of the response and separate them
                content.append(line);
                content.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        String cont = content.toString();
        String s = "";
        cont = cont.substring(cont.indexOf("\"display_name\""));
        cont = cont.substring(0, cont.indexOf("\",")+1);
        cont = cont.substring(cont.indexOf("\":")+3,cont.lastIndexOf("\""));
        String[] pairs = cont.split(",");
        Address ad = new Address();
        ad.street = pairs[1].trim()+" "+pairs[0].trim();
        ad.postalCode = pairs[pairs.length-2].trim();
        ad.city = pairs[pairs.length-5].trim();
        return ad;
    }
    
    String get() throws IOException {
        try {
            this.connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Get request from " + this.baseUrl);
        }

        return getInput();
    }
    Address getA() throws IOException {
        try {
            this.connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Get request from " + this.baseUrl);
        }

        return getAddress();
    }
    String postDriver() throws IOException {
        try {
            this.connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Post request from " + this.baseUrl);
        }

        // Set the doOutput flag to true
        connection.setDoOutput(true);

        return "Driver Creation ended with response : " + Integer.toString(connection.getResponseCode());
    }

    Order postOrder(Store store) throws IOException {
        try {
            this.connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Post request from " + this.baseUrl);
        }

        // Set the doOutput flag to true
        connection.setDoOutput(true);

        // Get the output stream of the connection instance
        // and add the parameter to the request
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            try {
                byte[] input = body.getBytes("utf-8");
                writer.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Always flush and close
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // To store our response
        StringBuilder content;

        // Get the input stream of the connection
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = input.readLine()) != null) {
                // Append each line of the response and separate them
                content.append(line);
                content.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        Map<String, String> myMap = new HashMap<String, String>();
        String cont = content.toString();
        String s = "";
        for (int i = 0; i < cont.length(); i++) {
            if (cont.charAt(i) != '{' && cont.charAt(i) != '}')
                s += cont.charAt(i);
        }
        
        String[] pairs = s.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            if(keyValue.length>1)
            myMap.put(keyValue[0], keyValue[1]);
        }
        
        Order order = new Order(myMap.get("\"uuid\""));
        System.out.println("order:"+order.orderId+" has been created with code:" + connection.getResponseCode());
        return order;
    }

    String post(List<Store> storeList) throws IOException {
        try {
            this.connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Post request from " + this.baseUrl);
        }

        // Set the doOutput flag to true
        connection.setDoOutput(true);

        // Get the output stream of the connection instance
        // and add the parameter to the request
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            try {
                byte[] input = body.getBytes("utf-8");
                writer.write(input, 0, input.length);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Always flush and close
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // To store our response
        StringBuilder content;

        // Get the input stream of the connection
        try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = input.readLine()) != null) {
                // Append each line of the response and separate them
                content.append(line);
                content.append(System.lineSeparator());
            }
        } finally {
            connection.disconnect();
        }
        Map<String, String> myMap = new HashMap<String, String>();
        String cont = content.toString();
        String s = "";
        for (int i = 0; i < cont.length(); i++) {
            if (cont.charAt(i) != '{' && cont.charAt(i) != '}')
                s += cont.charAt(i);
        }
        String[] pairs = s.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            myMap.put(keyValue[0], keyValue[1]);
        }
        Store store = new Store(myMap.get("\"store_id\""), myMap.get("\"store_number\""));
        storeList.add(store);
        return "store "+store.storeNumber+" is created with code :"+connection.getResponseCode();
    }
    String put() throws IOException {
        try {
            this.connection.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Post request from " + this.baseUrl);
        }

        // Set the doOutput flag to true
        connection.setDoOutput(true);

        // Get the output stream of the connection instance
        // and add the parameter to the request
        try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
            try {
                byte[] input = body.getBytes("utf-8");
                writer.write(input, 0, input.length);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            // Always flush and close
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return "updated with code : "+this.connection.getResponseCode();
    }


}
