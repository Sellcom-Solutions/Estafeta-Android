package com.sellcom.apps.tracker_material.Async_Request;

public enum METHOD {
    REQUEST_ZIPCODE ("1"),
    REQUEST_ZIPCODE_ADDRESSES ("r_zip_code_addresses"),
    REQUEST_TRACKING_LIST_CODES ("r_tracking_list_codes"),
    REQUEST_TRACKING_LIST_GUIDES ("r_tracking_list_guides"),
    REQUEST_NATIONAL_DELIVERY ("r_national_delivery"),
    REQUEST_INTERNATIONAL_DELIVERY ("r_international_delivery"),
    REQUEST_OFFICES ("r_offices"),
    TEST ("test"),
    REQUEST_EXCEPTION_CODES ("r_exception_codes");

    private final String name;

    private METHOD(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
        return name;
    }

}


