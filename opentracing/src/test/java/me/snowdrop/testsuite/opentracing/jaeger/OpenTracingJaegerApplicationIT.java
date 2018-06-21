/*
 *
 *  Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package me.snowdrop.testsuite.opentracing.jaeger;

import static com.jayway.restassured.RestAssured.when;

import com.jayway.restassured.RestAssured;
import java.util.concurrent.TimeUnit;
import org.arquillian.cube.openshift.impl.enricher.AwaitRoute;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.awaitility.Awaitility;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestTemplate;

@RunWith(Arquillian.class)
public class OpenTracingJaegerApplicationIT {

  private RestTemplate restTemplate = new RestTemplate();

  @RouteURL("${app.name}")
  @AwaitRoute(path = "/info")
  private String appRouteURL;

  @RouteURL("jaeger-query")
  private String jaegerQueryRouteURL;

  @Before
  public void setup() throws Exception {
    RestAssured.baseURI = appRouteURL + "hello";
  }

  @Test
  public void invokeHello() {
    when().get()
        .then()
        .statusCode(200);

    waitForJaegerQueryToReportTrace();
  }

  private void waitForJaegerQueryToReportTrace() {
    Awaitility.await().atMost(30, TimeUnit.SECONDS).until(() -> {
      try {
        final String output = restTemplate.getForObject(
            String.format("%sapi/traces?service=demo-opentracing-jaeger", jaegerQueryRouteURL),
            String.class
        );
        return output.contains("hello");
      } catch (Exception e) {
        return false;
      }
    });
  }

}
