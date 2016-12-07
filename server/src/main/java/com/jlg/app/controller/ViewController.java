package com.jlg.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.io.IOException;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ViewController {

    @Autowired
    ResourceUrlProvider resourceUrlProvider;

    @Autowired
    ResourceLoader resourceLoader;

    @RequestMapping(method = GET, path = "/")
    public ModelAndView home() throws IOException {
        ;
        HashMap<String, String> partials = new HashMap<>();
//    partials.put("foo", resourceUrlProvider.getForLookupPath("/partials/bar.html"));
        asList(
                ((GenericApplicationContext) resourceLoader).getResources("classpath:static/partials/**/*"))
                .stream()
                .filter(r -> getResourceUri(r).endsWith(".html"))
                .forEach(r -> {
                    String uri = getResourceUri(r);
                    int idx = uri.indexOf("/static");
                    partials.put(r.getFilename().replace(".html", ""), uri.substring(idx + 7));
                });

        HashMap<String, Object> model = new HashMap<>();
        model.put("partials", partials);
        return new ModelAndView("home", model);
    }

    private String getResourceUri(Resource resource) {
        try {
            return resource.getURI().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
