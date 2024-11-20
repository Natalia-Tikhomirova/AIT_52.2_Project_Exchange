import dao.OperationsImpl;
import static dao.OperationsImpl.loadTransactions;
import static dao.OperationsImpl.saveTransactions;

// Пункт обмена валюты:
// - количество валют - не боле 10
// - основная валюта - EUR
// - маржинальность при обмене - 5% от биржевого курса
// - добавление/удаление/редактирование и поиск транзакций по обмену валюты по датам в интервале ОТ и ДО
// - сохранение и считывание списка транзакций по обмену валюты из файла
// виды транзакций:
// - продажа
// - покупка
// - просмотр отчета по объему транзакций по обмену валюты по виду транзакций:
// - продажа
// - покупка

public class CurrencyExchangeAppl {

    public static void main(String[] args) {


        //каждый запуск начинается с загрузки информации
        OperationsImpl transaction = loadTransactions();

        //работа с программой
        transaction.startMenu();

        // при завершении работы авто-сохранение
        saveTransactions(transaction);

    } // end of main

} // end of class
