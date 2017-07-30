package com.vbashur.catalogue.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@org.immutables.value.Value.Immutable
@JsonSerialize(as = ImmutableCurrencyRate.class)
@JsonDeserialize(as = ImmutableCurrencyRate.class)
public interface CurrencyRate {

    String getBase();

    String getDate();

    Map<String, Double> getRates();
}
