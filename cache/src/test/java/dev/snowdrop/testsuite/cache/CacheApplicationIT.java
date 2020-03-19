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

import io.fabric8.kubernetes.api.model.v4_0.PodList;
import io.fabric8.kubernetes.clnt.v4_0.KubernetesClient;
import io.restassured.RestAssured;
import org.arquillian.cube.kubernetes.api.Session;
import org.arquillian.cube.openshift.impl.enricher.AwaitRoute;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * OpenShift integration test for cache application.
 */
@RunWith(Arquillian.class)
public class CacheApplicationIT extends CacheApplicationTestBase{

    @RouteURL("${app.name}")
    @AwaitRoute(path = "/favicon.ico")
    private String routeURL;

    @ArquillianResource
    private KubernetesClient client;

    @ArquillianResource
    private PodList pods;

    @ArquillianResource
    private Session session;

    private ByteArrayOutputStream stream = new ByteArrayOutputStream();

    @Before
    public void setup() throws Exception {
        RestAssured.baseURI = routeURL + "user";

        pods.getItems().forEach(p -> {
            final String name = p.getMetadata().getName();
            if (name.contains("build") || name.contains("deploy")) {
                return;
            }
            System.out.println("pod is: " + name);

            client
                    .pods()
                    .inNamespace(session.getNamespace())
                    .withName(name)
                    .tailingLines(1000)
                    .watchLog(stream);

        });
    }

    @Override
    protected String getLogs() {
        try {
            stream.close();
        } catch (IOException ignored) {}
        return new String(stream.toByteArray());
    }
}
