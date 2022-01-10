package org.jose.services.exchangesrates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.ehcache.CacheManager;
import org.jose.config.ApiConfig;
import org.jose.models.ExchangeRates;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootApplication
@ContextConfiguration(classes = {ApiConfig.class, ExchangeRatesConfig.class})
@TestPropertySource(locations = "/application.properties")
@UseAdviceWith
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ExchangeRatesRouteTest {

    public static final String RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON = "src/test/resources/services/exchangerates/successResponse-sortPositiveRecordDate.json";

    public static final String RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON = "src/test/resources/services/exchangerates/successResponse-sortNegativeRecordDate.json";

    public static final String EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON = "src/test/resources/services/exchangerates/expectedSuccessResponse-sortPositiveRecordDate.json";

    public static final String EXPECTED_RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON = "src/test/resources/services/exchangerates/expectedSuccessResponse-sortNegativeRecordDate.json";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRatesRouteTest.class);

    private static final String RECORD_DATE_POSITIVE = "record_date";

    private static final String RECORD_DATE_NEGATIVE = "-record_date";

    private static final String MOCK_ENDPOINT = "mock:endpoint";

    @Value("${http.treasury.url}")
    String endpoint;

    @EndpointInject(MOCK_ENDPOINT)
    MockEndpoint mockEndpoint;

    @Autowired
    CamelContext context;

    @Autowired
    ApiConfig apiConfig;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    ExchangeRatesConfig exchangeRatesConfig;

    ObjectMapper objectMapper = new ObjectMapper();

    FluentProducerTemplate fluentProducerTemplate;

    /**
     * Integration test
     */
    @Disabled
    @Test
    public void testGetExchangeRatesSuccessSortPositiveRecordDateIntegrationTest() {
        this.context.start();
        this.fluentProducerTemplate = context.createFluentProducerTemplate();
        this.fluentProducerTemplate.setDefaultEndpoint(this.context.getEndpoint(ExchangeRatesConstants.ROUTE));

        Exchange exchange = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .send();
        assertNotNull(exchange.getIn().getBody());
        assertNotNull(((ExchangeRates) exchange.getIn().getBody()).getData());
        assertNotNull(((ExchangeRates) exchange.getIn().getBody()).getLinks());
        assertNotNull(((ExchangeRates) exchange.getIn().getBody()).getMeta());
        this.context.stop();
    }

    @Test
    public void testGetExchangeRatesSuccessSortNegativeRecordDate() throws Exception {

        AdviceWith.adviceWith(this.context, this.context.getRoute(ExchangeRatesConstants.NAME),
                advice -> advice.interceptSendToEndpoint(endpoint + ".*")
                .skipSendToOriginalEndpoint()
                .to(MOCK_ENDPOINT));

        this.context.start();
        this.mockEndpoint.reset();

        this.fluentProducerTemplate = context.createFluentProducerTemplate();
        this.fluentProducerTemplate.setDefaultEndpoint(this.context.getEndpoint(ExchangeRatesConstants.ROUTE));

        this.mockEndpoint.expectedHeaderReceived(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_NEGATIVE);

        ExchangeRates response = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_NEGATIVE)
                .withBody(getJson(RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        String expectedResponse = getJson(EXPECTED_RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON);

        this.mockEndpoint.assertIsSatisfied();
        assertEquals(expectedResponse, actualResponse);

        this.context.stop();
    }

    @Test
    public void testGetExchangeRatesSuccessSortPositiveRecordDate() throws Exception {
        AdviceWith.adviceWith(this.context, this.context.getRoute(ExchangeRatesConstants.NAME),
                advice -> advice.interceptSendToEndpoint(endpoint + ".*")
                .skipSendToOriginalEndpoint()
                .to(MOCK_ENDPOINT));

        this.context.start();
        this.mockEndpoint.reset();

        this.fluentProducerTemplate = context.createFluentProducerTemplate();
        this.fluentProducerTemplate.setDefaultEndpoint(this.context.getEndpoint(ExchangeRatesConstants.ROUTE));

        this.mockEndpoint.expectedHeaderReceived(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE);

        ExchangeRates response = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .withBody(getJson(RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        String expectedResponse = getJson(EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON);

        this.mockEndpoint.assertIsSatisfied();
        assertEquals(expectedResponse, actualResponse);

        this.context.stop();
    }

    @Test
    public void testGetExchangeRatesSuccessCache() throws Exception {
        AdviceWith.adviceWith(this.context, this.context.getRoute(ExchangeRatesConstants.NAME),
                advice -> advice.interceptSendToEndpoint(endpoint + ".*")
                        .skipSendToOriginalEndpoint()
                        .to(MOCK_ENDPOINT));

        this.context.start();

        // Response not cached. Endpoint called.
        this.mockEndpoint.reset();
        this.mockEndpoint.setExpectedCount(1);
        this.mockEndpoint.expectedHeaderReceived(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE);

        this.fluentProducerTemplate = context.createFluentProducerTemplate();
        this.fluentProducerTemplate.setDefaultEndpoint(this.context.getEndpoint(ExchangeRatesConstants.ROUTE));

        ExchangeRates notCachedResponse = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .withBody(getJson(RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualNotCachedResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(notCachedResponse);
        String expectedResponse = getJson(EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON);

        assertEquals(expectedResponse, actualNotCachedResponse);
        this.mockEndpoint.assertIsSatisfied();

        // Response is cached. Endpoint not called.
        this.mockEndpoint.reset();
        this.mockEndpoint.setExpectedCount(0);
        this.mockEndpoint.setAssertPeriod(1);

        ExchangeRates cachedResponse = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .withBody(getJson(RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualCachedResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cachedResponse);
        expectedResponse = getJson(EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON);

        assertEquals(expectedResponse, actualCachedResponse);
        this.mockEndpoint.assertIsSatisfied();

        // Response not cached. It has different key. Endpoint gets called.
        this.mockEndpoint.reset();
        this.mockEndpoint.setExpectedCount(1);
        this.mockEndpoint.expectedHeaderReceived(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_NEGATIVE);

        ExchangeRates notCachedResponseNegativeSort = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_NEGATIVE)
                .withBody(getJson(RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        expectedResponse = getJson(EXPECTED_RESPONSE_SORT_NEGATIVE_RECORD_DATE_JSON);
        String actualNotCachedResponseNegativeSort = this.objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(notCachedResponseNegativeSort);

        assertEquals(actualNotCachedResponseNegativeSort, expectedResponse);
        this.mockEndpoint.assertIsSatisfied();

        this.context.stop();
    }

    @Test
    public void testCacheConfiguration() throws Exception {
        AdviceWith.adviceWith(this.context, this.context.getRoute(ExchangeRatesConstants.NAME),
                advice -> advice.interceptSendToEndpoint(endpoint + ".*")
                        .skipSendToOriginalEndpoint()
                        .to(MOCK_ENDPOINT));

        this.context.start();

        // Response not cached. Endpoint called.
        this.mockEndpoint.reset();
        this.mockEndpoint.setExpectedCount(1);
        this.mockEndpoint.expectedHeaderReceived(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE);

        this.fluentProducerTemplate = context.createFluentProducerTemplate();
        this.fluentProducerTemplate.setDefaultEndpoint(this.context.getEndpoint(ExchangeRatesConstants.ROUTE));

        ExchangeRates notCachedResponse = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .withBody(getJson(RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualNotCachedResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(notCachedResponse);
        String expectedResponse = getJson(EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON);

        assertEquals(expectedResponse, actualNotCachedResponse);
        this.mockEndpoint.assertIsSatisfied();

        // Response is cached. Endpoint not called.
        this.mockEndpoint.reset();
        this.mockEndpoint.setExpectedCount(0);
        this.mockEndpoint.setAssertPeriod(1);

        ExchangeRates cachedResponse = this.fluentProducerTemplate.to(ExchangeRatesConstants.ROUTE)
                .withHeader(ExchangeRatesConstants.SORT_HEADER, RECORD_DATE_POSITIVE)
                .withBody(getJson(RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON))
                .request(ExchangeRates.class);

        String actualCachedResponse = this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cachedResponse);
        expectedResponse = getJson(EXPECTED_RESPONSE_SORT_POSITIVE_RECORD_DATE_JSON);

        assertEquals(expectedResponse, actualCachedResponse);
        this.mockEndpoint.assertIsSatisfied();
        boolean isCached = this.cacheManager.getCache("exchangeRates", Object.class, Object.class).containsKey(RECORD_DATE_POSITIVE);
        assertTrue(isCached);
        this.context.stop();
    }

    private static String getJson(String path) {
        String json = null;
        try {
            json = Files.readString(Path.of(path));
        } catch (Exception exception) {
            LOGGER.error("^^^ Exception: " + exception.getMessage());
        }
        return json;
    }
}