package com.stocks.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class StockDataModel {
    private Calendar date;
    private BigDecimal price;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal annualyeild;
    private String symbol;
    private String exchange;
    private String descitpion;
    private String currency;
    private String industry;
    private String marketCap;

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    private String risk;

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getAnnualyeild() {
        return annualyeild;
    }

    public void setAnnualyeild(BigDecimal annualyeild) {
        this.annualyeild = annualyeild;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getDescitpion() {
        return descitpion;
    }

    public void setDescitpion(String descitpion) {
        this.descitpion = descitpion;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    @Override
    public String toString() {
        return "date=" + date.getTime() + ", price=" + price + ", bid=" + bid + ", ask=" + ask + ", low=" + low
                + ", high=" + high + ", open=" + open + ", annualyeild=" + annualyeild + ", symbol=" + symbol
                + ", exchange=" + exchange + ", descitpion=" + descitpion + ", currency=" + currency + ", industry="
                + industry + ", marketCap=" + marketCap + ", risk=" + risk;
    }


}
