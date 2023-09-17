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
            result.put("min_pricePerMonth", this.minPricePerMonth);
        }

        if(this.maxPricePerMonth != null){
            result.put("max_pricePerMonth", this.maxPricePerMonth);
        }

        if(this.minMinutes != null){
            result.put("min_minutes", this.minMinutes);
        }

        if(this.maxMinutes != null){
            result.put("max_minutes", this.maxMinutes);
        }

        if(this.minSms != null){
            result.put("min_sms", this.minSms);
        }

        if(this.maxSms != null){
            result.put("max_sms", this.maxSms);
        }

        return result;
    }
}
