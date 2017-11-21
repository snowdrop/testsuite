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

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.cloud.netflix.ribbon.StaticServerList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Ribbon configuration.
 *
 * @author Obsidian Quickstarts
 */
@Configuration
public class RibbonConfiguration {

    /**
     * PingUrl will ping a URL to check the status of each server.
     * Say Hello has, as you’ll recall, a method mapped to the /path; that means that Ribbon will get an HTTP 200 response when it pings a running Backend Server
     *
     * @return The URL to be used for the Ping
     */
    @Bean
    public IPing ribbonPing() {
        return new PingUrl();
    }

    /**
     * @return The Load Balancer rule
     */
    @Bean
    public IRule ribbonRule() {
        return new RandomRule();
    }

    /* Uncomment for local testing
    @Bean
    public ServerList<?> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", 8081));
        //return new StaticServerList<>(new Server("name-service", 80));
    }
    */
}
