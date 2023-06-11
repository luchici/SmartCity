package com.github.luchici.thymeleaf.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
public class ThymeleafConfig{

    // TODO: Inspect for more and find other ways to do this. What is by default?
    public SpringTemplateEngine thymeleafLayoutDialect(){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new LayoutDialect());
        return engine;
    }
}
