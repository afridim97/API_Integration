package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class HotelHandler implements RequestHandler<String, String>{
    private AmazonDynamoDB amazonDynamoDB;

    private static final String DYNAMODB_TABLE_NAME = "Hotels";
    private Regions REGION = Regions.AP_SOUTH_1;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public String handleRequest(String hotelCreateRequest, Context context)
    {   if(hotelCreateRequest == null){
            return "null??";
        }

        this.initDynamoDbClient();
        Hotel hotel = new Hotel(hotelCreateRequest);
        if(!hotel.isValid()){
            return "Please enter valid details to add the hotel information";
        }


        return persistData(hotel);
    }

    private String persistData(Hotel hotelCreateRequest){

        Map<String,AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
        String res = "";
        attributeValueMap.put("Name",new AttributeValue(hotelCreateRequest.getHotelName()));
        attributeValueMap.put("City",new AttributeValue(hotelCreateRequest.getCityName()));
        attributeValueMap.put("Price",new AttributeValue(String.valueOf(hotelCreateRequest.getPrice())));
        attributeValueMap.put("Stars",new AttributeValue(String.valueOf(hotelCreateRequest.getStars())));


        boolean exceptionThrown = false;
        try {
            this.amazonDynamoDB.putItem(DYNAMODB_TABLE_NAME, attributeValueMap);
        }
        catch (Exception e){
            System.err.println(e.getMessage() + "\n " + "Stack Trace: " + e.getStackTrace());
            exceptionThrown = true;
        }

        if(exceptionThrown){
            res = "There was an error in uploading the hotel details to the database";
        }
        else{
            res = "The hotel details were successfully uploaded!";
        }

        return res;
    }
    private void initDynamoDbClient(){

        try {
            this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(REGION)
                    .build();
        }
        catch(Exception e){
            System.err.println(e.getMessage() + "\n " + "Stack Trace: " + e.getStackTrace());
        }

    }
}