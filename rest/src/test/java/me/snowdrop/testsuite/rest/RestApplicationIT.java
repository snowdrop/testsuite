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

package me.snowdrop.testsuite.rest;

import com.jayway.restassured.RestAssured;
import io.openshift.booster.test.OpenShiftTestAssistant;
import me.snowdrop.testsuite.common.utils.OpenShiftUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * RestApplication Open Shift integration test.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class RestApplicationIT extends RestApplicationTestBase {

    private static final OpenShiftTestAssistant ASSISTANT = new OpenShiftTestAssistant();

    @BeforeClass
    public static void prepare() throws Exception {
        ASSISTANT.deployApplication();
        ASSISTANT.awaitApplicationReadinessOrFail();
        RestAssured.baseURI = RestAssured.baseURI + "/greeting";
        OpenShiftUtils.waitForApplication(RestAssured.baseURI);
    }

    @AfterClass
    public static void cleanup() {
        ASSISTANT.cleanup();
    }

}
