package org.barons.client;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: Oktay CEKMEZ
 * Date: 7.06.2022
 * Time: 21:20
 */
public interface GreetingController {
    @RequestMapping("/greeting")
    String greeting();
}