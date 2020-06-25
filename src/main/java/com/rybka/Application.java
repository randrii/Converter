package com.rybka;

import com.rybka.view.ExchangeView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        var context = SpringApplication.run(Application.class, args);
        var view = context.getBean(ExchangeView.class);

        while (true) {
            view.showView();
        }
    }

}
