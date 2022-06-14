package com.taxi.now.APIGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class PaymentRestController {

    @RequestMapping(value = "/payment_info", method = RequestMethod.POST )
    public void getPaymentInfo(@RequestParam Map<String, String> requestParams)
    throws Exception {
        String card_holder = requestParams.get("card_holder");
        String card_number = requestParams.get("card_number");
        String expiration_month = requestParams.get("expiration_month");
        String expiration_year = requestParams.get("expiration_year");
        String CVV = requestParams.get("CVV")
        System.out.println("Card holder: " +  card_number +
                "\n Card number :" + card_number +
                "\n Expiration month : "+ expiration_month + "\n Expiration year:" + expiration_year + "\n CVV:" + CVV);
        
       
        
        return ;
    }


}
