package com.vbashur.catalogue.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@org.immutables.value.Value.Immutable
@JsonSerialize(as = ImmutableCurrency.class)
@JsonDeserialize(as = ImmutableCurrency.class)
public interface Currency {

    String getBase();

    String getDate();

    Map<String, Double> getRates();
}
