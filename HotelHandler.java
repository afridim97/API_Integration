package org.example;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;

public class HotelHandler implements RequestHandler<String, String>,RequestStreamHandler{
    private JSONParser parser = new JSONParser();
    private static final String DYNAMODB_TABLE_NAME = "Hotels";
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    Database db;
    HotelHandler(){
        db = new Database();
        db.initDynamoDbClient();
    }
    public String handlePutHotel(String hotelCreateRequest, Context context)
    {   if(hotelCreateRequest == null){
            return "Please enter valid details to add the hotel information";
        }

        Hotel hotel = new Hotel(hotelCreateRequest);
        if(!hotel.isValid()){
            return "Please enter valid details to add the hotel information";
        }


        return db.persistData(hotel);
    }

    public void handleGetByParam(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();

        DynamoDB dynamoDb = new DynamoDB(db.amazonDynamoDB);
        Item result = null;
        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            JSONObject responseBody = new JSONObject();

            if (event.get("pathParameters") != null) {

                JSONObject pps = (JSONObject) event.get("pathParameters");
                if (pps.get("Name") != null) {

                    String hotelName = (String) pps.get("Name");
                    result = dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                            .getItem("Name", hotelName);
                }

            } else if (event.get("queryStringParameters") != null) {

                JSONObject qps = (JSONObject) event.get("queryStringParameters");
                if (qps.get("Name") != null) {

                    String hotelName = (String) qps.get("Name");
                    result = dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                            .getItem("Name", hotelName);
                }
            }
            if (result != null) {
                responseBody.put("Hotel", result.toJSONPretty());
                responseJson.put("statusCode", 200);
            } else {

                responseBody.put("message", "No item found");
                responseJson.put("statusCode", 404);
            }

            JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");

            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }


    //Required override
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

    }
    //Required override
    @Override
    public String handleRequest(String s, Context context) {
        return null;
    }
}