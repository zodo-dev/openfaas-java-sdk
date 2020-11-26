package dev.zodo.openfaas.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionInfo {
    private String name;
    private String image;
    private Integer invocationCount;
    private Integer replicas;
    private Integer availableReplicas;
    private String envProcess;
    private Map<String, String> labels;
    private Map<String, String> annotations;
}
