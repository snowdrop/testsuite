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

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public abstract class CacheApplicationTestBase {

    protected abstract String getLogs();

    /**
     * Ensures that the cache works correctly by inspecting the logs of UserService
     */
    @Test
    public void testCachePerformCorrectly() {
        final List<Integer> ids = Arrays.asList(1, 10, 100, 2, 5);

        //request each id 10 times
        IntStream.range(0, 10).forEach(index -> {
            ids.forEach(id -> assertValidResponse(id, perform(id)));
        });

        //assert that for each there was only was cache miss
        final String logOutput =  getLogs();
        ids.forEach(id -> assertThat(logOutput).containsOnlyOnce(expectedCacheMissLog(id)));
    }

    private String expectedCacheMissLog(int id) {
        return id + " not found in cache";
    }

    private void assertValidResponse(int id, ValidatableResponse response) {
        response
            .statusCode(is(200))
            .contentType(ContentType.JSON)
            .body("id", is(id));
    }

    private ValidatableResponse perform(int id) {
        return
                when()
                  .get("/{id}", id)
                  .then();
    }

}
