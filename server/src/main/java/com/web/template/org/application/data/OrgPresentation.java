package com.web.template.org.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrgPresentation {

    @JsonProperty("text")
    private String text;

    @JsonProperty("expanded")
    private Boolean expanded;

    @JsonProperty("children")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<OrgPresentation> children;

    @JsonProperty("leaf")
    private Boolean leaf;

    public void addChildren(OrgPresentation orgPresentation){
        this.children.add(orgPresentation);
    }
}
