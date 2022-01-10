package org.jose.services.exchangesrates;

import org.apache.camel.Header;
import org.jose.models.ExchangeRates;

import static org.jose.services.exchangesrates.ExchangeRatesConstants.SORT_HEADER;

public interface ExchangeRatesService {

    ExchangeRates getExchangeRates(@Header(SORT_HEADER) String sort);
}
