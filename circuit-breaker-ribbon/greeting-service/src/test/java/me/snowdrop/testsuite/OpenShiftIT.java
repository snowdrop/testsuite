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

package me.snowdrop.testsuite;

import me.snowdrop.testsuite.common.utils.OpenShiftUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

/**
 * Hystrix with Ribbon integration test on OpenShift.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class OpenShiftIT {

    private static final OpenShiftTestAssistant nameServiceAssistant = new OpenShiftTestAssistant("name-service", "../name-service/target/classes/META-INF/fabric8/openshift.yml");

    private static final OpenShiftTestAssistant greetingServiceAssistant = new OpenShiftTestAssistant("greeting-service", "target/classes/META-INF/fabric8/openshift.yml");

    private static String greetingServiceUrl;

    @BeforeClass
    public static void prepare() throws Exception {
        String nameServiceUrl = nameServiceAssistant.deployApplication();
        greetingServiceUrl = greetingServiceAssistant.deployApplication();
        nameServiceAssistant.awaitApplicationReadinessOrFail();
        greetingServiceAssistant.awaitApplicationReadinessOrFail();
        OpenShiftUtils.waitForApplication(nameServiceUrl);
        OpenShiftUtils.waitForApplication(greetingServiceUrl);
    }

    @AfterClass
    public static void cleanup() {
        nameServiceAssistant.cleanup();
        greetingServiceAssistant.cleanup();
    }

    @Test
    public void testThatWeServeAsExpected() {
        get(greetingServiceUrl + "/greeting")
                .then()
                .body(containsString("Hello from"))
                .body(not(containsString("Fallback")));
    }

    @Test
    public void testThatWeFallbackOnDelay() {
        get(greetingServiceUrl + "/greeting?delay=3000")
                .then()
                .body(equalTo("Hello from Fallback!"));
    }

}
