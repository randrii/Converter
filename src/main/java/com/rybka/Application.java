package com.rybka;

import com.rybka.configuration.ApplicationConfiguration;
import com.rybka.configuration.PersistenceJPAConfiguration;
import com.rybka.view.ExchangeView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        var context = new AnnotationConfigApplicationContext(
                ApplicationConfiguration.class,
                PersistenceJPAConfiguration.class);

        var view = (ExchangeView) context.getBean("exchangeView");

        while (true) {
            view.showView();
        }
    }

}
