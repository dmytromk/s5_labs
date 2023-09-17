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

        if(this.minPricePerMonth != null){
            result.put("minPricePerMonth", this.minPricePerMonth);
        }

        if(this.maxPricePerMonth != null){
            result.put("maxPricePerMonth", this.maxPricePerMonth);
        }

        if(this.minDataAllowanceMb != null){
            result.put("minDataAllowanceMb", this.minDataAllowanceMb);
        }

        if(this.maxDataAllowanceMb != null){
            result.put("maxDataAllowanceMb", this.maxDataAllowanceMb);
        }

        if(this.minSpeedMbps != null){
            result.put("minSpeedMbps", this.minSpeedMbps);
        }

        if(this.maxSpeedMbps != null){
            result.put("maxSpeedMbps", this.maxSpeedMbps);
        }

        return result;
    }
}
