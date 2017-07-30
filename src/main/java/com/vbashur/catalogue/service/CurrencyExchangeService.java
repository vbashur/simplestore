package com.vbashur.catalogue.service;

import com.vbashur.catalogue.model.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class CurrencyExchangeService {

    public final static String CURRENCY_ENDPOINT_URL = "http://api.fixer.io/latest?base=";

    @Autowired
    private RestTemplate restTemplate;

    public Double getRate(Double initialValue, String initialCurrency, String targetCurrency) {
        if (initialCurrency.equals(targetCurrency)) {
            return initialValue;
        }
        CurrencyRate currency = restTemplate.getForObject(CURRENCY_ENDPOINT_URL + initialCurrency, CurrencyRate.class);
        if (Objects.isNull(currency)) {
            throw new IllegalArgumentException(initialCurrency + " initial currency is not suported");
        }
        if (!currency.getRates().containsKey(targetCurrency)) {
            throw new IllegalArgumentException(targetCurrency + " target currency is not suported");
        }
        Double rate = currency.getRates().get(targetCurrency);
        return rate * initialValue;

    }

}
