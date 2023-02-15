package org.example;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import java.util.HashMap;
import java.util.Map;

public class Database {
    protected AmazonDynamoDB amazonDynamoDB;
    private static final String DYNAMODB_TABLE_NAME = "Hotels";
    private Regions REGION = Regions.AP_SOUTH_1;
    protected String persistData(Hotel hotelCreateRequest){

        Map<String, AttributeValue> attributeValueMap = new HashMap<String, AttributeValue>();
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

    protected void initDynamoDbClient(){

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
