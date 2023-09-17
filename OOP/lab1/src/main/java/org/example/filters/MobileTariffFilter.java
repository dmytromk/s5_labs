package org.example.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class MobileTariffFilter implements Filter {
    private Integer minPricePerMonth;
    private Integer maxPricePerMonth;
    private Integer minMinutes;
    private Integer maxMinutes;
    private Integer minSms;
    private Integer maxSms;


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        if(minPricePerMonth != null){
            result.put("minPricePerMonth", minPricePerMonth);
        }

        if(maxPricePerMonth != null){
            result.put("maxPricePerMonth", maxPricePerMonth);
        }

        if(minMinutes != null){
            result.put("minMinutes", minMinutes);
        }

        if(maxMinutes != null){
            result.put("maxMinutes", maxMinutes);
        }

        if(minSms != null){
            result.put("minSms", minSms);
        }

        if(maxSms != null){
            result.put("maxSms", maxSms);
        }

        return result;
    }
}
