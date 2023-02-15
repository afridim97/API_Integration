package org.example;


import com.google.gson.Gson;

public class Hotel {

    String hotelName;
    String cityName;
    Double stars;
    Double price;

    public Hotel(String hotelDetails) {
        String[] hotelDetailsArr = hotelDetails.split(",");
        this.hotelName = hotelDetailsArr[0];
        this.cityName = hotelDetailsArr[1];
        this.price = Double.parseDouble(hotelDetailsArr[2]);
        this.stars = Double.parseDouble(hotelDetailsArr[3]);
    }
    public String getHotelName(){
        return hotelName;
    }

    public String getCityName(){
        return cityName;
    }


    public Double getPrice() {
        return price;
    }

    public Double getStars(){
        return stars;
    }

    public boolean isValid(){
        return this.getCityName() != null && this.getHotelName() != null && this.getPrice() != 0 && this.getStars() != 0;
    }
}
