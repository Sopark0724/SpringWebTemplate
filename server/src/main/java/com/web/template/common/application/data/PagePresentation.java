package com.web.template.common.application.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class PagePresentation<T> {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("results")
    private long results;

    @JsonProperty("items")
    private List<T> items;
}
