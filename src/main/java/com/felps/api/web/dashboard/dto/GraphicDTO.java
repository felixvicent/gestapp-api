package com.felps.api.web.dashboard.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphicDTO {
  private String category;
  private List<GraphicValueDTO> values;
}
