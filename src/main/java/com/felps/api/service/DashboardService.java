package com.felps.api.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felps.api.model.CategoryType;
import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.TransactionRepository;
import com.felps.api.web.dashboard.dto.GraphicDTO;

@Service
public class DashboardService {
  @Autowired
  private TransactionRepository transactionRepository;

  public List<GraphicDTO> getGraphicData(CategoryType type, UserAccount user) {
    List<Transaction> transactions = transactionRepository.findByTypeUserAndDate(type.toString(), user.getId(), 2023);

    List<GraphicDTO> graphic = new ArrayList<GraphicDTO>();

    for (int month = 1; month <= 12; month++) {
      final int innerMonth = month;
      double totalValue = transactions.stream()
          .filter(transaction -> transaction.getDatetime().getMonthValue() == innerMonth)
          .map(transaction -> transaction.getValue())
          .reduce(0.0, (acc, el) -> acc + el);

      graphic.add(GraphicDTO.builder()
          .month(new DateFormatSymbols().getMonths()[innerMonth - 1])
          .value(totalValue)
          .build());

    }

    return graphic;
  }
}
