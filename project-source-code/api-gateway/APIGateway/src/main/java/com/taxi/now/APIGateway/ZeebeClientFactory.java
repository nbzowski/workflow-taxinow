package com.taxi.now.APIGateway; // CHANGE as needed to match whatever the name of the package is that you have created

import io.camunda.zeebe.client.ZeebeClient;

public class ZeebeClientFactory {

    public static ZeebeClient getZeebeClient() {
        return ZeebeClient.newCloudClientBuilder()
                .withClusterId("10f14fca-c94d-40e7-be13-18e9a6803cf1")
                .withClientId("pf6E68Bi.K9gQ~VQWgE.Qt22fpOfe6df")
                .withClientSecret("P~.yRr.J3_daVPeb4MSWzyyF4duLtF6uUmm0vpNcsxW4HWByO6SxdnXpK8r-avCl")
                .withRegion("bru-2")
                .build();
    }

}