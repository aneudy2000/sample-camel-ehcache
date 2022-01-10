package org.jose.services.exchangesrates;

public class ExchangeRatesConstants {

    public static final String SORT_HEADER = "sortHeader";

    public static final String NAME = "exchangeRates";

    public static final String ROUTE = "direct:" + NAME;

    public static final String CACHE_ROUTE = "direct:" + NAME + "Cache";

    public static final String CACHE = "ehcache:" + NAME;
}
