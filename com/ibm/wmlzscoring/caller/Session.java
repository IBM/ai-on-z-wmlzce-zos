package com.ibm.wmlzscoring.caller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/*
 * (C) Copyright IBM Corp. 2022.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

public class Session {
    private static final String PREDICT = "/prediction";
    private static final String INPUT = "input";
    private static final String API = "/v1/api";
    private static final Integer OK = 200;
    private String host;
    private String port;
    private String token;
    private String model;

    public Session(String host, String port, String token, String model) {
        this.host = host;
        this.port = port;
        this.token = token;
        this.model = model;
    }

    /**
    * Format input and send a POST request to the WMLz OSCE instance to get a predicition. 
    *
    * This is intended for use with the example model provided alongside this Java sample. 
    * It will need changes to support different models and other configurations.
    *
    * WMLz OSCE API documentation can be found here:
    * https://www.ibm.com/products/machine-learning-for-zos
    *
    * @param inTensor  the input to run a predicition against the model.
    * @return          a string containing the predicition
    */
    public String predict(String inTensor) {
        String result = "";
        String url = ("http://" + this.host + ":" + this.port + API + this.model + PREDICT);
        System.out.println("Serving URL is: " + url);
        try {
            System.out.println("Building POST request");
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            
            // Send the input to be parsed
            JsonArrayBuilder jab = parsePlainTextInput(inTensor);

            // Create the payload for TensorFlow
            String payload = Json.createArrayBuilder().add(Json.createObjectBuilder()
                .add(INPUT, Json.createArrayBuilder().add(jab)))
                .build()
                .toString();

            StringEntity entity = new StringEntity(payload);  
            System.out.println("Payload created as: " + payload);

            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", this.token);
            System.out.println("POST request will be: " + httpPost.toString());

            // Execute the request
            CloseableHttpResponse response = client.execute(httpPost);
            
            // Make sure the response worked - code == 200
            if (response.getStatusLine().getStatusCode() != OK)
                System.out.println("Something went wrong! Response: " + response.toString());
            
            HttpEntity responseEntity = response.getEntity();
            result = EntityUtils.toString(responseEntity);
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
    * Parse the tensor provided as input, and cready a JsonArray of the decimal (double)
    * values found in a comma separated list.
    *   
    * In a real world use case that handles structured and/or transactional data, you would have
    * different logic based on the requirements of the service and/or model you are calling. 
    *
    * This is a straightforward mechanism to parse the input expected by our model. 
    *
    * @param tensor  the input tensor to parse
    * @return        the parsed input tensor as a JsonArrayBuilder object
    */
    private JsonArrayBuilder parsePlainTextInput(String tensor){
        JsonArrayBuilder jarray = Json.createArrayBuilder();
        List<String> convertedCountriesList = Arrays.asList(tensor.split(",", -1));
        for (String entry : convertedCountriesList) {
            jarray.add(Double.parseDouble(entry));
        }
        return jarray;
    }


    //
    // Getters and Setters
    //


    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
	
}
