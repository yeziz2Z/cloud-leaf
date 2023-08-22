package com.leaf.oauth2.security.jackson2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class LongMixin {
    @JsonCreator
    public static Long create(@JsonProperty("value") Long value) {
        return value;
    }

    @JsonProperty("value")
    public abstract Long getValue();
}
