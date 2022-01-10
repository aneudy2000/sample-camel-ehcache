package org.jose.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataItem {
    @JsonProperty("record_date")
    private String recordDate;

    private String country;

    private String currency;

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDesc;

    @JsonProperty("exchange_rate")
    private String exchangeRate;

    @JsonProperty("effective_date")
    private String effectiveDate;

    @JsonProperty("src_line_nbr")
    private String srcLineNbr;

    @JsonProperty("record_fiscal_year")
    private String recordFiscalYear;

    @JsonProperty("record_fiscal_quarter")
    private String recordFiscalQuarter;

    @JsonProperty("record_calendar_year")
    private String recordCalendarYear;

    @JsonProperty("record_calendar_quarter")
    private String recordCalendarQuarter;

    @JsonProperty("record_calendar_month")
    private String recordCalendarMonth;

    @JsonProperty("record_calendar_day")
    private String recordCalendarDay;

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCountryCurrencyDesc() {
        return countryCurrencyDesc;
    }

    public void setCountryCurrencyDesc(String countryCurrencyDesc) {
        this.countryCurrencyDesc = countryCurrencyDesc;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getSrcLineNbr() {
        return srcLineNbr;
    }

    public void setSrcLineNbr(String srcLineNbr) {
        this.srcLineNbr = srcLineNbr;
    }

    public String getRecordFiscalYear() {
        return recordFiscalYear;
    }

    public void setRecordFiscalYear(String recordFiscalYear) {
        this.recordFiscalYear = recordFiscalYear;
    }

    public String getRecordFiscalQuarter() {
        return recordFiscalQuarter;
    }

    public void setRecordFiscalQuarter(String recordFiscalQuarter) {
        this.recordFiscalQuarter = recordFiscalQuarter;
    }

    public String getRecordCalendarYear() {
        return recordCalendarYear;
    }

    public void setRecordCalendarYear(String recordCalendarYear) {
        this.recordCalendarYear = recordCalendarYear;
    }

    public String getRecordCalendarQuarter() {
        return recordCalendarQuarter;
    }

    public void setRecordCalendarQuarter(String recordCalendarQuarter) {
        this.recordCalendarQuarter = recordCalendarQuarter;
    }

    public String getRecordCalendarMonth() {
        return recordCalendarMonth;
    }

    public void setRecordCalendarMonth(String recordCalendarMonth) {
        this.recordCalendarMonth = recordCalendarMonth;
    }

    public String getRecordCalendarDay() {
        return recordCalendarDay;
    }

    public void setRecordCalendarDay(String recordCalendarDay) {
        this.recordCalendarDay = recordCalendarDay;
    }
}
