package com.leekyoungil.webp.sample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by Kellin on 3/18/16.
 */
@Controller
public class FileUploadViewController {

    @RequestMapping(value = {"/upload", "/"}, method = RequestMethod.GET)
    public String getUploadView (Map<String, Object> model) {

        return "fileUploadView";
    }
}
