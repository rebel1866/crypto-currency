package com.idf.controller.listener;

import com.idf.service.logic.CurrencyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class ContextStartedListener implements ApplicationListener<ApplicationStartedEvent> {
    private CurrencyLogic currencyLogic;

    @Autowired
    public void setCurrencyLogic(CurrencyLogic currencyLogic) {
        this.currencyLogic = currencyLogic;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            while (true) {
                System.out.println("start");
                currencyLogic.updateCurrencyPrices();
                System.out.println("updated");
                TimeUnit.SECONDS.sleep(10);
            }
        });
    }
}
