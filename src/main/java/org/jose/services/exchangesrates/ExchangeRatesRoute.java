package org.jose.services.exchangesrates;

import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ehcache.EhcacheConstants;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.jose.config.ApiConfig;
import org.jose.models.ExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;

import static org.jose.services.exchangesrates.ExchangeRatesConstants.*;

public class ExchangeRatesRoute extends RouteBuilder {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private ExchangeRatesConfig exchangeRatesConfig;

    Processor logProcessor(String message) {
        return exchange -> {
            System.out.println("<><>" + message);
        };
    }

    @Override
    public void configure() throws Exception {
        final String url = this.apiConfig.getTreasuryUrl();
        final String endpoint = this.exchangeRatesConfig.getEndpoint();
        final String exchangeEndpoint = url + endpoint;

        from(CACHE_ROUTE).to(CACHE + "?cacheManager=#cacheManager");

        from(ROUTE).routeId(NAME)
                .setHeader(EhcacheConstants.KEY, simple("${header." + ExchangeRatesConstants.SORT_HEADER + "}"))
                .setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_GET))
                .to(CACHE_ROUTE)
                .choice()
                    .when(header(EhcacheConstants.ACTION_HAS_RESULT).isEqualTo("false"))
                        .process(this.logProcessor("NOT cached"))
                        .toD(exchangeEndpoint + "?sort=${header." + ExchangeRatesConstants.SORT_HEADER + "}")
                        .unmarshal(new JacksonDataFormat(ExchangeRates.class))
                        .setHeader(EhcacheConstants.ACTION, constant(EhcacheConstants.ACTION_PUT))
                        .to(CACHE_ROUTE)
                    .otherwise()
                        .process(this.logProcessor("Found in cache"))
                .end();
    }
}
