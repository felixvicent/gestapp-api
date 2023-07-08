package com.felps.api.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felps.api.model.Category;
import com.felps.api.model.CategoryType;
import com.felps.api.model.Transaction;
import com.felps.api.model.UserAccount;
import com.felps.api.repository.CategoryRepository;
import com.felps.api.repository.TransactionRepository;
import com.felps.api.web.dashboard.dto.GraphicDTO;
import com.felps.api.web.dashboard.dto.GraphicValueDTO;

@Service
public class DashboardService {
  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  public List<GraphicDTO> getGraphicData(CategoryType type, UserAccount user) {
    List<GraphicDTO> graphic = new ArrayList<GraphicDTO>();

    GraphicDTO allTransactions = getGraphicValueData(type, user);

    graphic.add(allTransactions);

    List<Category> categories = categoryRepository.findByUserAndType(user, type);

    categories.stream().forEach(category -> {
      var values = getGraphicValueData(category, user);

      graphic.add(values);
    });

    // for (int month = 1; month <= 12; month++) {
    // final int innerMonth = month;
    // double totalValue = allTransactions.stream()
    // .filter(transaction -> transaction.getDatetime().getMonthValue() ==
    // innerMonth)
    // .map(transaction -> transaction.getValue())
    // .reduce(0.0, (acc, el) -> acc + el);

    // graphic.add(GraphicDTO.builder()
    // .month(new DateFormatSymbols().getMonths()[innerMonth - 1])
    // .value(totalValue)
    // .build());

    // }

    return graphic;
  }

  private GraphicDTO getGraphicValueData(CategoryType type, UserAccount user) {
    List<Transaction> allTransactions = transactionRepository.findByTypeUserAndDate(type.toString(), user.getId(),
        2023);

    List<GraphicValueDTO> graphic = new ArrayList<GraphicValueDTO>();

    for (int month = 1; month <= 12; month++) {
      final int innerMonth = month;
      double totalValue = allTransactions.stream()
          .filter(transaction -> transaction.getDatetime().getMonthValue() == innerMonth)
          .map(transaction -> transaction.getValue())
          .reduce(0.0, (acc, el) -> acc + el);

      graphic.add(GraphicValueDTO.builder()
          .month(new DateFormatSymbols().getMonths()[innerMonth - 1])
          .value(totalValue)
          .build());
    }

    String message = type == CategoryType.EXPENSE ? "Gasto total": "Ganho total";

    return GraphicDTO.builder().category(message).values(graphic).build();
  }

  private GraphicDTO getGraphicValueData(Category category, UserAccount user) {
    List<Transaction> allTransactions = transactionRepository.findByCategoryAndTypeAndDatetime(category.getId(),
        user.getId(),
        2023);

    List<GraphicValueDTO> graphic = new ArrayList<GraphicValueDTO>();

    for (int month = 1; month <= 12; month++) {
      final int innerMonth = month;
      double totalValue = allTransactions.stream()
          .filter(transaction -> transaction.getDatetime().getMonthValue() == innerMonth)
          .map(transaction -> transaction.getValue())
          .reduce(0.0, (acc, el) -> acc + el);

      graphic.add(GraphicValueDTO.builder()
          .month(new DateFormatSymbols().getMonths()[innerMonth - 1])
          .value(totalValue)
          .build());
    }

    return GraphicDTO.builder().category(category.getTitle()).values(graphic).build();
  }
}
