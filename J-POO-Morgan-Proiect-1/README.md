#### Copyright 2024 Popescu Mihnea-Gabriel 321CA

## Description
This project is a simulation of a simple banking system. The system is composed of a bank,
which has a number of users. The users can have multiple accounts, each of them with multiple
cards. They can peform various operations on their accounts, such as sending money, paying online,
split the payments. They can also add or delete accounts or cards. Each account has a unique currency,
and the user can exchange money between accounts with different currencies.

## Classes and Packages
I partitioned this project in the following way:
- commands package which has all the classes that implement the commands that the user can give
- users package which has all the classes that implement the users and their accounts and cards
- the Start class in main package which reads the input and calls the appropriate methods

### Commands Package
Each of the commands extend the CommandBase abstract class, which has a method that is
called by the Start class. The Start class reads the input and calls the method of the
command that the user wants to execute. The `CommandFactory` class dynamically creates
and executes commands based on the user's input. It uses a switch statement to identify
the correct command and delegates execution. This modular approach makes it easy to add
new commands while keeping the system organized and extensible. They are implemented in
such way to be able to implement new features easily in the future.

### Users Package
This package contains the classes that implement the users, accounts, cards, transactions and
the currency graph. The currency graph is implemented as a directed graph, where the edges
represent the exchange rate between two currencies. The graph is used to calculate the exchange
rate between two currencies using Dijkstra's algorithm. The User class has a list of accounts,
and each account has a list of cards. The User class also has a list of transactions, which
keeps track of all the transactions that the user has made.

### Flow
The flow of the banking system begins with the **Start class** reading the user's input.
The input contains the command the user wants to execute along with any necessary parameters.
Based on this input, the **CommandFactory** is called, which identifies the appropriate
command class to execute. The factory dynamically creates and invokes the corresponding command.

The `Builder` class is used to simplify the creation of command objects in the `CommandFactory`.
It follows the **Builder pattern**, allowing for the construction of objects like commands
with multiple optional and mandatory parameters. This class helps organize the dependencies required
by commands, such as users, transactions, and currency graph, without forcing the caller to pass all
arguments every time. It provides various constructors to set different combinations of parameters,
making it flexible and extensible for different command scenarios. This approach improves readability,
maintainability, and the ability to add new commands in the future.

Each command extends the **CommandBase** class and is responsible for performing specific
operations on the user’s accounts and cards. Depending on the operation, commands can manipulate
account balances, perform currency exchanges, create or delete accounts, and more. After the command
is executed, the results are stored in the **output** array and returned to the user.

The **User** class manages user-related data, such as their list of accounts, cards, and transactions.
The **Account** class handles the operations related to individual accounts, and the **Card** class
deals with card-related actions. The **CurrencyGraph** enables currency exchange between accounts
with different currencies, using Dijkstra’s algorithm to calculate the conversion rate.

Once the operation is completed, the **output** is populated with the necessary data and returned
to the user, providing feedback on the success or failure of the operation.

### Key Classes
- **Start**: The entry point of the system. It initializes the system with user data and commands,
then delegates execution to the `CommandFactory`, which handles command execution and stores results.

- **CommandFactory**: A factory class that creates and executes commands based on the user's input.
It uses the Builder pattern to inject dependencies into commands and calls the correct command class.

- **CommandBase**: An abstract class for all commands, defining a common structure and ensuring
consistency in how commands are executed.

- **User**: Represents a user in the system, holding personal data and a list of accounts and
transactions. It facilitates user-specific operations.

- **Account**: Represents a bank account, including details like IBAN, balance, and currency.
It handles operations like balance manipulation and currency conversion.

- **Card**: Represents a card linked to an account, with attributes such as card number, status,
and actions like freezing or deleting cards.

- **Transaction**: Represents a financial transaction, tracking the amount, sender/receiver,
and timestamp. It logs financial activity for auditing.

- **CurrencyGraph**: A directed graph storing exchange rates between currencies, using Dijkstra's
algorithm to facilitate currency conversions.

- **Builder**: Simplifies the creation of command objects, allowing for flexible and clear
construction of commands with optional parameters.

### OOP Principles
- Inheritance
- Polymorphism
- Patterns : Factory Pattern, Builder Pattern
- Packages
- Lombok
- Overriding
- Abstract classes
- Encapsulation