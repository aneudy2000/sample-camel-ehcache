package org.jose.services.exchangesrates;

import org.apache.camel.builder.RouteBuilder;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.net.URL;

import static org.jose.services.exchangesrates.ExchangeRatesConstants.NAME;

@Configuration
public class ExchangeRatesConfig {

    @Value("${http.treasury.rates-of-exchange.endpoint}")
    private String endpoint;

    @Bean(value = "cacheManager")
    @Scope("singleton")
    CacheManager getCacheManager() {
        URL url = getClass().getResource("/ehcache.xml");
        org.ehcache.config.Configuration xmlConfig = new XmlConfiguration(url);
        CacheManager cacheManager = CacheManagerBuilder.newCacheManager(xmlConfig);
        cacheManager.init();
        return cacheManager;
    }

    @Bean(value = NAME + "/routeBuilder")
    RouteBuilder getExchangeRatesRouteBuilder() {
        return new ExchangeRatesRoute();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
