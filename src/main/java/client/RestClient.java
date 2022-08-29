package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .addHeader("Content-type", "application/json")
                .setBaseUri(BASE_URL)
                .build();
    }

}
