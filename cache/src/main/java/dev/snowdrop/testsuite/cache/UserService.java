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

package dev.snowdrop.testsuite.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Service providing access to  {@link User}
 */
@Component
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserNameGenerator userNameGenerator;

    public UserService(UserNameGenerator userNameGenerator) {
        this.userNameGenerator = userNameGenerator;
    }

    /**
     * Each invocation will be cached by id
     */
    @Cacheable("users")
    public User findById(int id) {
        log.info("id: {} not found in cache", id);
        return new User(id, userNameGenerator.generate());
    }
}
