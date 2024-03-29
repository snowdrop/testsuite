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

package dev.snowdrop.testsuite.rest.resteasy;

import org.junit.Test;

import static io.restassured.RestAssured.when;
import static org.hamcrest.core.Is.is;

/**
 * Base RestApplication test class shared by unit and integration test classes.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public abstract class RestApplicationTestBase {

    @Test
    public void shouldGetHelloWorld() {
        when().get()
                .then()
                .statusCode(200)
                .body("content", is("Hello, World!"));
    }

}
