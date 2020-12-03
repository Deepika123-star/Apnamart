package com.smartwebarts.apnamart.helpers;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class HelpDataDump {

    private Context context;

    public HelpDataDump(Context context) {
        this.context = context;
    }

    public LinkedHashMap<String, List<String>> getDataGeneral() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        expandableListDetail.put("Men", null);

        expandableListDetail.put("Women", null);

        List<String> tokenRules = new ArrayList<String>();
        tokenRules.add("Multipurpose Sanitizers");
        tokenRules.add("Protective Eyewear");
        tokenRules.add("Essentials Combo Kits");

        expandableListDetail.put("Prevention Gear", tokenRules);

        List<String> travelRules = new ArrayList<String>();
        travelRules.add("Designer Hard covers");
        travelRules.add("Premium Glass Covers");

        expandableListDetail.put("Mobile Covers", travelRules);

        return expandableListDetail;
    }

}
