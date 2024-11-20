package dao;

import model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface Operations {

    void mainMenu();

    Transaction addTrans(int num, String name, boolean type, LocalDate date, double res, double marge);

    boolean removeTrans(int num);

    Transaction findTrans(int num);

    List<Transaction> findTransByDate(LocalDate dateFrom, LocalDate dateTo);

    List<Transaction> findTransByType(String valuta, boolean type);

    int quantity();

    double calcRes(String name, double amount);

    double calcMarge(String name);

    void printAllTrans(); // выводит все транзакции, которые хранятся в списке transactions

    void printNewListTrans(List <Transaction> newList); // выводит транзакции из переданного списка newList

}  // end of class