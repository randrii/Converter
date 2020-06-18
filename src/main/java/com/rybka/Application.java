package com.rybka;

import com.rybka.configuration.ApplicationConfiguration;
import com.rybka.configuration.PersistenceJPAConfiguration;
import com.rybka.view.ExchangeView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {

        var context = new AnnotationConfigApplicationContext(
                ApplicationConfiguration.class,
                PersistenceJPAConfiguration.class);

        var view = (ExchangeView) context.getBean("exchangeView");

        while (true) {
            view.showView();
        }
    }

}
