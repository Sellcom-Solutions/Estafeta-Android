package com.sellcom.apps.tracker_material.Async_Request;

public enum METHOD {
    SEND_TRACKING ("login"),
    GET_USER_PDVS ("get_user_pdvs"),
    USER_CHECK_IN ("user_checkin"),
    USER_CHECK_OUT ("user_checkout"),
    GET_PRODUCTS ("get_products"),
    SET_ORDER ("set_order"),
    GET_BUDGET_CATS ("get_budget_categories"),
    GET_BUDGET ("get_budget"),
    GET_RECOMMENDATIONS ("get_recommendations"),
    LOGOUT ("logout"),
    ;

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


