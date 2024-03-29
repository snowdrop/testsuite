/*
 *
 *  * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package dev.snowdrop.testsuite.cache;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CacheApplicationTest extends CacheApplicationTestBase {

    @Rule
    public final OutputCaptureRule outputCapture = new OutputCaptureRule();

    @Value("${local.server.port}")
    private int port;

    @Before
    public void setup() {
        RestAssured.baseURI = String.format("http://localhost:%d/user/", port);
    }

    @Override
    protected String getLogs() {
        return outputCapture.toString();
    }
}
