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

package dev.snowdrop.testsuite.rest.resteasy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Greeting controller.
 *
 * @author <a href="mailto:cmoulliard@redhat.com">Charles Moulliard</a>
 */
@Component
@Path("/")
public class GreetingEndpoint {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private GreetingProperties properties;

    @GET
    @Path("/greeting")
    @Produces("application/json")
    public Greeting greeting(@DefaultValue("World") @QueryParam("name") String name) {
        return new Greeting(this.counter.incrementAndGet(), String.format(this.properties.getMessage(), name));
    }
}
