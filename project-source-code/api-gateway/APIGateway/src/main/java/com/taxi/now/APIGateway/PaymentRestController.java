package com.taxi.now.APIGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//We are creating payment rest controller to show payment
@RestController
@RequestMapping("/api/")
public class PaymentRestController {

    @PostMapping("/payment_info")
    @ResponseBody
    public List getPaymentInfo(@RequestParam(name = "payment_id") List<Integer> payment_id, @RequestParam(name="card_number") List<Integer> card_number, @RequestParam(name="card_holder") List<String> card_holder, @RequestParam(name="expiration_year") List<Integer> expiration_year, @RequestParam(name="expiration_month") List<Integer> expiration_month, @RequestParam(name="CVV") List<Integer> CVV) {
        //card_number, card_holder, expiration_year, expiration_month, CVV.
        List list = new ArrayList<>();
        list.add(payment_id);
        list.add(card_number);
        list.add(card_holder);
        list.add(expiration_month);
        list.add(expiration_year);
        list.add(CVV);
        System.out.println(payment_id);

        return list;
    }


}
