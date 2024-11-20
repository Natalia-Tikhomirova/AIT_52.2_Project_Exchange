package dao;

import model.Transaction;
import view.CurrencyExchange;
import view.StartMenu;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class OperationsImpl implements Operations, Serializable {

    transient Comparator<Transaction> compRes = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction o1, Transaction o2) {
            return Double.compare(o2.getRes(), o1.getRes());
        }
    };


    public static final String STORAGE = "current_exchange_list.dat";
    Scanner scanner;
    List<Transaction> transactions;
    public static String input;

    public OperationsImpl(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void startMenu() {
        System.out.println("******************************************************");
        System.out.println("******* Welcome to the Currency Exchange Point *******");
        System.out.println("******************************************************");

        StartMenu moneyday[] = StartMenu.values();
        boolean checker = true;
        boolean validChoice = false;

        while (checker) {

            StartMenu.printMenu();
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            if (input.matches("\\d+"))
            {    //  проверка на неправильный ввод
                int actionNum = Integer.parseInt(input);
                validChoice = true;

                LocalDate dateFrom;
                LocalDate dateTo;
                switch (actionNum) {
                    case 1 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction() + " Good !");
                        mainMenu();
                    }
                    case 2 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction() + " Input the number, please:");
                        int num = scanner.nextInt();
                        Transaction findedTrans = findTrans(num);
                        if (findedTrans == null) System.out.println("No such transaction.");
                        else System.out.println(findedTrans);
                    }
                    case 3 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction() + " All transactions:");
                        System.out.println();
                        printAllTrans();
                    }
                    case 4 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction() + "Input data, please");
                        System.out.println();
                        System.out.print("Input start date ");
                        dateFrom = dateFromUserToSystem();
                        System.out.print("\nInput finish date ");
                        dateTo = dateFromUserToSystem();
                        printNewListTrans(findTransByDate(dateFrom, dateTo));
                    }
                    case 5 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction());
                        int typeChoice;
                        do {
                            String valuta = chooseCurrency();
                            System.out.println();
                            typeChoice = chooseType();
                            if (typeChoice == 3) {
                                break;
                            }
                            boolean typeFin = typeChoice == 1 ? true : false;
                            //  findTransByType(valuta, typeFin);
                            printNewListTrans(findTransByType(valuta, typeFin));
                            int continueChoice;
                            do {
                                System.out.println("Press 1 - for continue, or 0 - for exit:");
                                continueChoice = scanner.nextInt();
                            } while (continueChoice != 0 && continueChoice != 1);

                            if (continueChoice == 0) {
                                System.out.println("Thank you for using our services!");
                                break;
                            }

                        } while (typeChoice != 3);
                    }
                    case 6 -> {
                        System.out.println(actionNum + ". " + moneyday[actionNum - 1].getAction() + ": input the transaction number, you want to remove:");
                        printAllTrans();
                        int numRemove = scanner.nextInt();
                        if (removeTrans(numRemove)) {
                            System.out.println(" Delete Successful !");
                            printAllTrans();
                        } else System.out.println("No such transaction");
                    }
                    case 7 -> {
                        checker = false;
                        System.out.println("Wait for you later. Bye !");
                        break;
                    }
                    default -> System.out.println("I'm sorry, don't understand you.");

                }//end actionNum
            } else System.out.println("Error. Input is no correct");
        }//end while checker

    }//end startMenu

    public LocalDate dateFromUserToSystem() {
        Scanner scanner = new Scanner(System.in);
        LocalDate date = null;
        String dateTemp;
        do {
            System.out.print(" (example 12-12-2022):");
            dateTemp = scanner.nextLine();
            if (!checkDateFormat(dateTemp)) {
                System.out.println("Incorect date format.");
            }//end if
        } while (!checkDateFormat(dateTemp));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[dd:MM:yyyy][dd/MM/yyyy][dd-MM-yyyy][dd.MM.yyyy][dd,MM,yyyy][dd_MM_yyyy]");
        try {
            date = LocalDate.parse(dateTemp, dtf);
        } catch (
                DateTimeParseException e) {
            System.out.println("Incorrect date format. Please try again.");
        }
        return date;
    }//end dateFromUserToSystem

    public boolean checkDateFormat(String str) {
        return str.matches("((((([0][1-9]|[1-2][0-9]|[3][0-1]))(-|.|/||:|,|_)(12))|(([0][1-9]|[1][0-9]|[2][0-8])(-|.|/||:|,|_)(02))|(([0][1-9]|[1-2][0-9]|30)(-|.|_)((01|[0][3-9]|[1][0-1])|)))(-|.|/||:|,|_)(([0-9][0-9][0-9][1-9]|[0-9][0-9][1-9][0-9]|[0-9][1-9][0-9][0-9]|[1-9][0-9][0-9][0-9])))");
    }//end checkDateFormatEU

    @Override
    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);

        int mainChoise = 0;
        double amountChoise = 0;
        String valuta = null;

        do {
            int typeChoice = chooseType();

            if (typeChoice == 3)
            {
                break;
            }

            do {
                System.out.println("Input sum: ");
                if (scanner.hasNextDouble()) {
                    amountChoise = scanner.nextDouble();
                    if (amountChoise > 0) {
                        break;  // корректная сумма
                    } else {
                        System.out.println("Amount must be greater than 0.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid number.");
                    scanner.nextLine();  // очистить буфер
                }
            } while (amountChoise <= 0);  // сумма должна быть больше 0

            valuta = chooseCurrency();  // валюта

            // если сумма положительная — это покупка, если отрицательная — это продажа
            if (typeChoice == 2) {
                amountChoise = amountChoise * (-1);  // продажа
            }

            double result = calcRes(valuta, amountChoise);
            double margeResult = calcMarge(valuta);

            // true или 1 - покупка, false или 2 - продажа
            boolean typeFin = (typeChoice == 1);

            int transactionNum = transactions.isEmpty() ? 1 : transactions.size() + 1;  // новый номер транзакции
            addTrans(transactionNum, valuta, typeFin, LocalDate.now(), result, margeResult);

            // продолжение работы
            do {
                System.out.println("Press 1 - for continue, or 0 - for exit:");
                if (scanner.hasNextInt()) {
                    mainChoise = scanner.nextInt();
                    break;  // если ввод корректен
                } else {
                    System.out.println("Invalid input. Please enter 1 or 0.");
                    scanner.nextLine();  // очистить буфер
                }
            } while (mainChoise != 0 && mainChoise != 1);

            if (mainChoise == 0) {
                System.out.println("See you soon ;)");
                break;
            }

        } while (mainChoise == 1);
    }

    public int chooseType() {
        // если сумма 'amount' положительная — это покупка, если отрицательная — это продажа.
        Scanner scanner = new Scanner(System.in);
        int typeChoice = 0;
        do {
            System.out.println("Press 1 - for sell, 2 - for buy, 3 - for exit:");
            if (scanner.hasNextInt())
            {
                typeChoice = scanner.nextInt();
                if (typeChoice == 1 || typeChoice == 2 || typeChoice == 3) {
                    break;
                }
            } else {
                // если ввели не число
                System.out.println("Invalid input.");
                scanner.nextLine();
            }
        } while (typeChoice != 1 && typeChoice != 2 && typeChoice != 3);
        return typeChoice;
    }//end chooseType


    public String chooseCurrency() {
        String valuta = null;
        boolean validInput = false;

        // Continuously prompt the user until a valid choice is made
        while (!validInput) {
            System.out.println("Choose currency, please:");

            // Display the currency options
            int tempNum = 1;
            for (CurrencyExchange choise : CurrencyExchange.values()) {
                System.out.println(" press " + tempNum++ + " - for " + choise.getCurrency_codes());
            }

            // Read user input
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.matches("\\d+")) {
                int currencyChoice = Integer.parseInt(input);

                if (currencyChoice >= 1 && currencyChoice <= CurrencyExchange.values().length) {
                    // Valid choice, set the currency
                    switch (currencyChoice) {
                        case 1 -> valuta = "USD";
                        case 2 -> valuta = "AUD";
                        case 3 -> valuta = "EGP";
                        case 4 -> valuta = "KZT";
                        case 5 -> valuta = "GBP";
                        case 6 -> valuta = "FJD";
                        default -> valuta = null; // Default case, although shouldn't reach here
                    }
                    validInput = true; // Exit the loop after successful input
                } else {
                    System.out.println("Invalid currency choice. Please select a valid option.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return valuta;

    }//end chooseCurrency

    @Override
    public Transaction addTrans(int num, String name, boolean type, LocalDate date, double res, double marge) {
        transactions.add(transactions.size(), new Transaction(num, name, type, date, res, marge));
        return transactions.get(transactions.size()-1);
    }//end addTrans

    @Override
    public boolean removeTrans(int num) {
        // Ищем транзакцию по номеру и удаляем
        transactions.sort(Comparator.comparing(Transaction::getNumber));
        boolean removed = transactions.removeIf(transaction -> transaction.getNumber() == num);

        if (removed)
        {
            // после удаления всех транзакций перенумеровываем их!!!!
            int newNumber = 1; // Начинаем с 1
            for (Transaction transaction : transactions)
            {
                transaction.setNumber(newNumber++);
            }
        }
        return removed;
    }

    @Override
    public Transaction findTrans(int num) {
        // Возвращаем транзакцию по номеру
        for (Transaction transaction : transactions) {
            if (transaction.getNumber() == num) {
                return transaction;
            }
        }
        return null; // Если не найдена
    }

    @Override
    public List<Transaction> findTransByDate(LocalDate dateFrom, LocalDate dateTo) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
            if ((date.isEqual(dateFrom) || date.isAfter(dateFrom)) &&
                    (date.isEqual(dateTo) || date.isBefore(dateTo)))
            {
                result.add(transaction);
            }
        }
        return result;
    }

    @Override
    public List<Transaction> findTransByType(String valuta, boolean type) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.isType() == type && transaction.getName().equals(valuta)) {
                result.add(transaction);
            }
        }
        transactions.sort(compRes);
        return result;

    }//end findTransByType


    @Override
    public int quantity() {
        return transactions.size();
    }


    @Override
    public double calcRes(String name, double amount) // название валюты и сумма для обмена
    // если сумма 'amount' положительная — это покупка, если отрицательная — это продажа.
    {
        CurrencyExchange currency = null; // ищем курс в enum
        for (CurrencyExchange ce : CurrencyExchange.values())
        {
            if (ce.getCurrency_codes().equals(name)) // название валюты "name" передано в этот метод
            {
                currency = ce;
                break;
            }
        }
        // если валюта не найдена, сообщаем об этом и возвращаем 0
        if (currency == null) {
            System.out.println("Currency with code " + name + " not found");
            return 0;
        }
        // получили курс из enum для валюты
        double rate = currency.getCurrent_exchange();
        // маржа для курса валюты, через метод calcMarge
        double margin = calcMarge(name);
        double rateWithMargin = 0; // курс с маржей
        if (amount < 0) { //  если < 0 = продажа, иначе покупка
            rateWithMargin = rate - margin;  // если продажа, то маржа вычитается
        } else {
            rateWithMargin = rate + margin;  // если покупка, то маржа добавляется
        }
        //  результат обмена с учетом маржи
        double result = 0;
        if (amount < 0) {  // опять, если сумма отрицательная — это продажа
            result = Math.abs(amount) * rateWithMargin;  // продаем валюту и получаем евро
        } else {  // Покупка
            result = Math.abs(amount) / rateWithMargin;  // покупаем валюту за евро
        }
        System.out.println("--------------------------------------------------------");
        System.out.printf("\u001B[31mResult of the exchange %.2f  EUR\u001B[0m\n", result);
        System.out.println("--------------------------------------------------------\n");
        return result;
    }
    @Override
    public double calcMarge(String currencyName)
    {
        //  курс по названию валюты
        CurrencyExchange currency = null;
        for (CurrencyExchange ce : CurrencyExchange.values()) {
            if (ce.getCurrency_codes().equals(currencyName)) {
                currency = ce;
                break;
            }
        }
        //  валюта не найдена, тогда -> 0
        if (currency == null) {
            System.out.println("Currency with code " + currencyName + " not found.");
            return 0;
        }
        // вытаскиваем курс для данной валюты
        double rate = currency.getCurrent_exchange();
        //  маржа (5% от курса)
        double margin = rate * 0.05;
        return margin;
    }


    @Override
    public void printAllTrans() {
        if (transactions.isEmpty()) {
            System.out.println("The transaction list is empty.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    public void printNewListTrans(List <Transaction> newList) {
        if (newList.isEmpty()) {
            System.out.println("The transaction list is empty.");
        } else {
            for (Transaction transaction : newList) {
                System.out.println(transaction);
            }
        }
    }


    public static OperationsImpl loadTransactions() {
        File file = new File(STORAGE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                System.out.println("Load Data from storage...");
                OperationsImpl ops = (OperationsImpl) ois.readObject();
                if (ops.transactions == null) {
                    ops.transactions = new ArrayList<>(); // Инициализируем список, если он пуст
                }
                return ops;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data: " + e.getMessage());
            }
        }
        // Если файла не существует
        return new OperationsImpl(new ArrayList<>()); // Инициализируем пустой список
    }

    public static void saveTransactions(OperationsImpl note) {
        try (ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(STORAGE))) {
            System.out.println("Saving data to storage...");
            ous.writeObject(note);
        } catch (IOException e) {
            System.out.println("Error while saving data: " + e.getMessage());
            e.printStackTrace();
        }

    } //saveTransactions

} // end of class