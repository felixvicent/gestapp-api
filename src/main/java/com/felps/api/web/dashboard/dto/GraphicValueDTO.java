package com.felps.api.web.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphicValueDTO {
  private double value;
  private String month;
}
