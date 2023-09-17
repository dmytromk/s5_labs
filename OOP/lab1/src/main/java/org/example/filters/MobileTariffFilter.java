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

        if(this.minPricePerMonth != null){
            result.put("minPricePerMonth", this.minPricePerMonth);
        }

        if(this.maxPricePerMonth != null){
            result.put("maxPricePerMonth", this.maxPricePerMonth);
        }

        if(this.minMinutes != null){
            result.put("minMinutes", this.minMinutes);
        }

        if(this.maxMinutes != null){
            result.put("maxMinutes", this.maxMinutes);
        }

        if(this.minSms != null){
            result.put("minSms", this.minSms);
        }

        if(this.maxSms != null){
            result.put("maxSms", this.maxSms);
        }

        return result;
    }
}
