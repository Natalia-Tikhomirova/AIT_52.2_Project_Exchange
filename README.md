# Currency Exchange Application

## Overview
This application allows users to exchange currencies and manage transactions. It is implemented in Java and provides a text-based interface for interacting with the application.

## Features
- View all transactions.
- Add a new transaction.
- Find transactions by date or type.
- Delete a transaction.
- Calculate currency exchange with margins.
- Save and load transactions from storage.

## Prerequisites
- Java Development Kit (JDK) 8 or higher.
- A terminal or command prompt to run the application.

## How to Run the Application
1. Clone the repository:
   ```bash
   git clone https://github.com/Natalia-Tikhomirova/currency-exchange.git
   ```
2. Navigate to the project directory:
   ```bash
   cd currency-exchange
   ```
3. Compile the Java files:
   ```bash
   javac Main.java
   ```
4. Run the application:
   ```bash
   java Main
   ```

## Usage Guide
When you start the application, a menu will appear with the following options:

1. **Start Exchange**
   - Initiates a new currency exchange transaction.
2. **Find a Transaction**
   - Allows you to search for a transaction by its unique number.
3. **View All Transactions**
   - Displays all recorded transactions.
4. **Find Transactions by Date**
   - Search for transactions within a specific date range.
5. **Find Transactions by Type**
   - Filter transactions by currency and type (buy/sell).
6. **Delete a Transaction**
   - Remove a transaction by its number and renumber remaining transactions.
7. **Exit**
   - Exit the application.

## Example Workflow
1. **Start a New Exchange**
   - Choose the type of transaction (buy/sell).
   - Enter the amount and select a currency.
   - The system calculates the result with a margin and saves the transaction.

2. **Find Transactions by Date**
   - Enter a start and end date in the format `dd-MM-yyyy`.
   - The system displays all transactions within the specified range.

3. **Delete a Transaction**
   - View all transactions to identify the transaction number.
   - Enter the number of the transaction to delete it.

## Future Improvements
- Add support for additional currencies.
- Implement a graphical user interface (GUI).
- Provide real-time exchange rate updates from an online API.

## License
This project is licensed under the MIT License.
