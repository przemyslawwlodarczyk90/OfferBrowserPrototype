package com.example.offerbrowserprototype.infrastructure.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards all non-API, non-static routes to index.html so React Router handles them.
 * Regex [^\\.] excludes paths with a dot (static assets like .js, .css, .png).
 */
@Controller
public class SpaController {

    @RequestMapping(value = {"/{path:[^\\.]*}", "/**/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }
}
