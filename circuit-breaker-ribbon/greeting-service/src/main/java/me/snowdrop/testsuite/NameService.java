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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Service invoking name-service via REST and guarded by Hystrix.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Service
public class NameService {

    @Value("${NameServiceHost:name-service}")
    private String host;
    @Value("${NameServicePort:-1}")
    private int port;

    private final RestTemplate restTemplate;

    public NameService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallbackName", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
            //@HystrixProperty(name = "execution.isolation.strategy", value="SEMAPHORE") // execute in the same Thread
    })
    public String getName(int delay) {
        String hostAndPort = host + (port > 0 ? (":" + port) : "");
        return this.restTemplate.getForObject(String.format("http://%s/name?delay=%d", hostAndPort, delay), String.class);
    }

    private String getFallbackName(int delay) {
        return "Fallback";
    }

}
