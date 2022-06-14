package com.taxi.now.APIGateway; // CHANGE as needed to match whatever the name of the package is that you have created

import io.camunda.zeebe.client.ZeebeClient;

public class ZeebeClientFactory {

    public static ZeebeClient getZeebeClient() {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId("a8434432-ba71-4f54-b4ab-aa03d19131e9")
                .withClientId("aMLk~WQhyZ5BgzOcIx7mDizztELApFAt")
                .withClientSecret("1PP6LP.F5~gRvKuU~lDd7KtC_4frOI56-IoeWVuAM3GT4bOnnxyQu~qBI_71p~9e")
                .withRegion("bru-2")
                .build();
    }

}
