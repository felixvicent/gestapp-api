package com.felps.api.web.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felps.api.model.CategoryType;
import com.felps.api.model.UserAccount;
import com.felps.api.service.DashboardService;
import com.felps.api.web.dashboard.dto.GraphicDTO;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
  @Autowired
  private DashboardService dashboardService;

  @GetMapping("/graphic/{type}")
  public ResponseEntity<List<GraphicDTO>> graphic(@AuthenticationPrincipal UserAccount user,
      @PathVariable CategoryType type) {
    List<GraphicDTO> graphic = dashboardService.getGraphicData(type, user);

    return ResponseEntity.ok().body(graphic);
  }
}
