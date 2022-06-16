package com.taxi.now.APIGateway;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PaymentRestController {

    @RequestMapping(value = "/payment_info", method = RequestMethod.POST )
    public void getPaymentInfo(@RequestParam Map<String, String> requestParams)
    throws Exception {
        String userSessionID="";
        String cardHolder = requestParams.get("cardHolder");
        String creditCardNum = requestParams.get("creditCardNum");
        String expireMonth = requestParams.get("expireMonth");
        String expireYear = requestParams.get("expireYear");
        String userId = requestParams.get("userId");
      //  String CVV = requestParams.get("CVV");
        System.out.println("User Id" + userId + "\n Card Holder: " +  cardHolder +
                "\n Card number: " + creditCardNum +
                "\n Expiration month: "+ expireMonth + "\n Expiration year: " + expireYear );

        try (ZeebeClient client = ZeebeClientFactory.getZeebeClient()) {
            client.newDeployResourceCommand()
                    .addResourceFromClasspath("workflow-bpmn.bpmn") // Filename of diagram in IntelliJ project resources folder
                    .send()
                    .join();

            final ProcessInstanceEvent event = client.newCreateInstanceCommand()
                    .bpmnProcessId("user-process")
                    .latestVersion()
                    .variables(Map.of("userID", userId, "userSessionID", userSessionID, "card_holder", cardHolder,"card_number", creditCardNum, "expiration_month", expireMonth, "expiration_year",expireYear))  // variables in "key1, value1, key2, value2" format
                    .send()
                    .join();

        }
        return ;
    }


}
