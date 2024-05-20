package com.example.mobile_store.constant;

public class ApiUrlConstant {
    public static final String API_VS1 = "/api/vs1";
    
    public static final String CATEGORY = API_VS1 + "/categories";
    public static final String IMAGE = API_VS1 + "/images";
    public static final String ORDER = API_VS1 + "/orders";
    public static final String ORDER_DETAIL = API_VS1 + "/orderDetails";
    public static final String PRODUCT = API_VS1 + "/products";
    public static final String PAYMENT = API_VS1 + "/payments";
    public static final String USER = API_VS1 + "/users";
    public static final String UPLOAD = API_VS1 + "/uploads";

    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";

    public static final String CREATE = "/create";
    public static final String READ = "/read";

    public static final String UPDATE_ID = "/update/{id}";
    public static final String READ_ID = "/read/{id}";
    public static final String DELETE_ID = "/delete/{id}";
}