package com.rybka;

import com.rybka.view.ExchangeView;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class Application {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

        ExchangeView view = (ExchangeView) context.getBean("exchangeView");

        while (true) {
            view.showView();
        }
    }

}
