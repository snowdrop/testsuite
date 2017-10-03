/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.snowdrop.testsuite.health;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

import com.jayway.restassured.RestAssured;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Health check application integration test with a healthy indicator.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@RunWith(Arquillian.class)
public class HealthCheckApplicationHealthyIT {

    @RouteURL("${app.name}")
    private String routeURL;

    @Before
    public void setup() throws Exception {
        RestAssured.baseURI = routeURL;
    }

    @Test
    public void shouldGetHealthyResponse() {
        when().get("/health")
                .then()
                .statusCode(is(200));
                // TODO - We get a shouldGetHealthyResponse:46 ? IllegalArgument
                //.body("status", is("UP"))
                //.body("custom.status", is("UP"))
                //.body("diskSpace.status", is("UP"));
    }

}
