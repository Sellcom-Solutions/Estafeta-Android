package com.sellcom.apps.tracker_material.Utils;

import java.util.HashMap;
import java.util.Map;

public class TrackerManager {
    private static 	TrackerManager   						manager;

    private Map<String,String> utils;

    private TrackerManager(){
        utils = new HashMap<>();
    }
    public static synchronized TrackerManager sharedInstance(){
        if (manager == null)
            manager = new TrackerManager();
        return manager;
    }
    public void setCurrent_pdv(Map<String,String> current_pdv){ this.utils = current_pdv;}
    public Map<String,String> getCurrent_pdv(){ return this.utils;}

    public void addInfoToCurrentUtils(String key, String value){
        utils.put(key,value);
    }

    public String getInfoFromCurrentUtilsFromKey(String key){
        return utils.get(key);
    }
}
