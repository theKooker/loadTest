package src.dispatch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

    String get() throws IOException {
        try {
            this.connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            System.err.println("(!) Error:Get request from " + this.baseUrl);
        }

        return getInput();
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

    String postOrder(Store store) throws IOException {
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

        System.out.println("content = " + s);
        String[] pairs = s.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            System.out.println(keyValue[0] + " xxx " + keyValue[1]);
            myMap.put(keyValue[0], keyValue[1]);
        }
        Order order = new Order(myMap.get("\"uuid\""));
        System.out.println(order);
        store.orders.add(order);
        return "order:"+order.orderId+" has been created with code:" + connection.getResponseCode();
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

        System.out.println("content = " + s);
        String[] pairs = s.split(",");
        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            System.out.println(keyValue[0] + " xxx " + keyValue[1]);
            myMap.put(keyValue[0], keyValue[1]);
        }
        Store store = new Store(myMap.get("\"store_id\""), myMap.get("\"store_number\""));
        System.out.println(store);
        storeList.add(store);
        return content.toString();
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

        return "order updated with code : "+this.connection.getResponseCode();
    }

}
