package org.jose.controllers;

import org.apache.camel.Produce;
import org.jose.models.ExchangeRates;
import org.jose.services.exchangesrates.ExchangeRatesConstants;
import org.jose.services.exchangesrates.ExchangeRatesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api")
public class TreasuryController {

    @Produce(value = ExchangeRatesConstants.ROUTE)
    private ExchangeRatesService exchangeRatesService;

    @GetMapping(value = "/treasury")
    public @ResponseBody
    ExchangeRates getTreasuryData(@RequestParam String sort) {
        return this.exchangeRatesService.getExchangeRates(sort);
    }
}
