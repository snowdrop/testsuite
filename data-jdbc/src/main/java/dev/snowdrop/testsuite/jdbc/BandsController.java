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

package dev.snowdrop.testsuite.jdbc;

import dev.snowdrop.testsuite.common.entities.Band;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * REST controller to access {@link Band} database records.
 *
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
@Controller
public class BandsController {

    private BandsRepository bandsRepository;

    @Autowired
    public BandsController(BandsRepository bandsRepository) {
        this.bandsRepository = bandsRepository;
    }

    @ResponseBody
    @GetMapping("/poke")
    public Band poke() {
        return new Band();
    }

    @ResponseBody
    @GetMapping("/bands/{id}")
    public Band get(@PathVariable("id") Long id) {
        return this.bandsRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bands")
    public void post(@RequestBody Band band) {
        System.out.println(String.format("Creating band with id=%d and name=%s", band.getId(), band.getName()));
        this.bandsRepository.insert(band);
    }
}
