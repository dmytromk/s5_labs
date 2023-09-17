package org.example.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class HomeTariffFilter implements Filter {
    private Integer minPricePerMonth;
    private Integer maxPricePerMonth;
    private Integer minDataAllowanceMb;
    private Integer maxDataAllowanceMb;
    private Integer minSpeedMbps;
    private Integer maxSpeedMbps;


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        if(minPricePerMonth != null){
            result.put("minPricePerMonth", minPricePerMonth);
        }

        if(maxPricePerMonth != null){
            result.put("maxPricePerMonth", maxPricePerMonth);
        }

        if(minDataAllowanceMb != null){
            result.put("minDataAllowanceMb", minDataAllowanceMb);
        }

        if(maxDataAllowanceMb != null){
            result.put("maxDataAllowanceMb", maxDataAllowanceMb);
        }

        if(minSpeedMbps != null){
            result.put("minSpeedMbps", minSpeedMbps);
        }

        if(maxSpeedMbps != null){
            result.put("maxSpeedMbps", maxSpeedMbps);
        }

        return result;
    }
}
