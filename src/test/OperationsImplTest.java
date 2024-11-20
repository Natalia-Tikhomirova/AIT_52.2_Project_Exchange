package test;

import dao.OperationsImpl;
import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CurrencyExchange;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperationsImplTest {

    private OperationsImpl operations;

    @BeforeEach
    void setUp() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, "USD", true, LocalDate.now(), 100, 1.5)); //продажа
        transactions.add(new Transaction(2, "EUR", false, LocalDate.now().minusDays(1), 200, 2.0)); //покупка
        transactions.add(new Transaction(3, "USD", true, LocalDate.now().minusDays(2), 300, 1.0)); //продажа
        transactions.add(new Transaction(4, "GPB", false, LocalDate.now().minusDays(3), 400, 1.2)); //покупка

        operations = new OperationsImpl(transactions);
    }

    @Test
    void addTrans() {
        int newTransNum = 5;
        Transaction add = operations.addTrans(4, "USD", true, null, 400, 4);
        assertEquals(4, add.getNumber());
    }


    @Test
    void removeTrans() {

        // Проверка, что транзакция с номером 2 существует до удаления
        Transaction transactionBeforeRemoval = operations.findTrans(2);
        assertNotNull(transactionBeforeRemoval);

        // Удаление транзакции с номером 2
        boolean res = operations.removeTrans(2);
        assertTrue(res, "Удаление транзакции с номером 2 должно быть успешным.");
        assertNotNull(operations.findTrans(2), "Транзакция с номером 2 должна отсутствовать.");

        // Проверка перенумерации
        Transaction firstTransaction = operations.findTrans(1);
        assertNotNull(firstTransaction, "Транзакция с номером 1 должна существовать.");
        assertEquals(1, firstTransaction.getNumber());

        // Удаление несуществующей транзакции
        boolean resNotFound = operations.removeTrans(111);
        assertFalse(resNotFound);
    }

    @Test
    void findTrans() {
        Transaction found = operations.findTrans(3);
        assertNotNull(found);
        assertEquals(3, found.getNumber());
        Transaction notFound = operations.findTrans(111);
    }

    @Test
    void findTransByDate() {
        LocalDate dateFrom = LocalDate.now().minusDays(2);
        LocalDate dateTo = LocalDate.now();
        List<Transaction> foundTrans = operations.findTransByDate(dateFrom, dateTo);
        assertNotNull(foundTrans);
        assertFalse(foundTrans.isEmpty());
        assertEquals(3, foundTrans.size());
    }

    @Test
    void findTransByType() {
        // Проверяем продажи для валюты "USD"
        List<Transaction> saleTransactions = operations.findTransByType("USD", true);
        assertEquals(2, saleTransactions.size());
        assertEquals("USD", saleTransactions.get(0).getName());
        assertEquals("USD", saleTransactions.get(1).getName());

        // Проверяем покупки для валюты "EUR"
        List<Transaction> eurBuyingTransactions = operations.findTransByType("EUR", false);
        assertEquals(1, eurBuyingTransactions.size());
        assertEquals("EUR", eurBuyingTransactions.get(0).getName());

        // Проверяем покупки для валюты "GPB"
        List<Transaction> gpbBuyingTransactions = operations.findTransByType("GPB", false);
        assertEquals(1, gpbBuyingTransactions.size());
        assertEquals("GPB", gpbBuyingTransactions.get(0).getName());
    }

    @Test
    void quantity() {
        int count = operations.quantity();
        // узнаем количество транзакций
        assertEquals(4, count);
    }


    @Test
    void calcRes() {
        // тестируем покупку USD
        double actualUsdBuying = operations.calcRes("USD", 100);

        double expectedUsdBuying = 100 / (CurrencyExchange.USD.getCurrent_exchange() + CurrencyExchange.USD.getCurrent_exchange() * 0.05);//узнаем сколько получим с покупки 100 долларов
        assertEquals(expectedUsdBuying, actualUsdBuying, 0.001);

        // продажa USD
        double actualUsdSell = operations.calcRes("USD", -100);
        double expectedUsdSell = 100 * (CurrencyExchange.USD.getCurrent_exchange() - CurrencyExchange.USD.getCurrent_exchange() * 0.05);// узнаем сколько получим с продажи 100 долларов
        assertEquals(expectedUsdSell, actualUsdSell, 0.001);

        // делаем тест на покупку не существующей валюты
        double invalidRes = operations.calcRes("INVALID", 100);
        assertEquals(0, invalidRes);// так как валюты не существует, то и результат получим 0
    }

    @Test
    void calcMarge() {
        // рассчитываем маржу для доллара
        double usdMargin = operations.calcMarge("USD");
        assertEquals(0.05 * CurrencyExchange.USD.getCurrent_exchange(), usdMargin, 0.001);

        // несуществующая валюта
        double invalidMargin = operations.calcMarge("INVALID");
        assertEquals(0, invalidMargin);// так как такой валюты нет, то и маржа будет 0
    }
} // end of class