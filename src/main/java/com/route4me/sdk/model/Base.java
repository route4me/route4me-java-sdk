package com.route4me.sdk.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author juan
 */
public abstract class Base {

    private Map<String, String> params = new HashMap<>();
    private DataObject data = null;
    private static volatile String apiKey;

    public static final Gson GSONDeserializer = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.registerTypeAdapter(DataObject.class, new DataObjectDeserializer())
			.create();
    public static final Gson GSONSerializer = new GsonBuilder()
            .registerTypeAdapter(DataObject.class, new DataObjectSerializer())
            .setPrettyPrinting().create();
    
    public Base(String apiKey) {
        Base.apiKey = apiKey;
    }


    /**
     * @return the params
     */
    public Map<String, String> getParams() {
        if (!params.containsKey("api_key")) {
            params.put("api_key", Base.apiKey);
        }
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * @return the data
     */
    public DataObject getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(DataObject data) {
        this.data = data;
    }
    
    public String getJsonData(){
        if (getData()!=null){
            String serializedData = GSONSerializer.toJson(data);
            System.out.println(serializedData);
            return serializedData;
        }
        return null;
    }


}
