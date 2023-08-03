package org.barons.client;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:20
 */
public interface GreetingController {
    @RequestMapping("/greeting")
    String greeting(@RequestHeader MultiValueMap<String, String> headers);
}