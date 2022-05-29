package com.taxi.now.APIGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//We are creating payment rest controller to show payment
@RestController
@RequestMapping("/api/payment")
public class PaymentRestController {
    //@Value("${string.port}")
    //private String port;

    @PostMapping("/info")
    @ResponseBody
    public List showPaymentInfo(@RequestParam(name = "payment_id") String payment_id) {
        //card_number, card_holder, expiration_year, expiration_month, CVV.
        List list = new ArrayList<>();
        list.add(payment_id);
        System.out.println(payment_id);
        return list;   //Return is NULL, WHY?
    }
//    public ResponseEntity<String> showPaymentInfo()
//    {
//        return ResponseEntity.ok("FROM PAYMENT SERVICE, Port# is:" + port);
//    }

}
