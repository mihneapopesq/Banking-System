package org.poo.utilities.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.utilities.users.BusinessAccount;
import org.poo.utilities.users.User;

import java.util.ArrayList;

public class BusinessReport extends CommandBase {

    private final ArrayList<User> users;
    private final CommandInput commandInput;
    private final ArrayList<BusinessAccount> businessAccounts;
    private final ObjectMapper objectMapper;
    private final ArrayNode output;

    public BusinessReport(final Builder builder) {
        this.users = builder.getUsers();
        this.commandInput = builder.getCommandInput();
        this.businessAccounts = builder.getBusinessAccounts();
        this.objectMapper = builder.getObjectMapper();
        this.output = builder.getOutput();
    }

    @Override
    public void execute() {
        for (BusinessAccount account : businessAccounts) {
            if (account.getIban().equals(commandInput.getAccount())) {
                ObjectNode reportNode = objectMapper.createObjectNode();

                reportNode.put("balance", account.getBalance());
                reportNode.put("currency", account.getCurrency());
                reportNode.put("deposit limit", account.getDepositLimit());
                reportNode.put("spending limit", account.getSpendingLimit());
                reportNode.put("statistics type", "transaction");
                reportNode.put("IBAN", account.getIban());

                double totalDeposited = 0;
                double totalSpent = 0;

                ArrayNode employeesArray = objectMapper.createArrayNode();
                for (User employee : account.getEmployees()) {
                    ObjectNode employeeNode = objectMapper.createObjectNode();
                    employeeNode.put("username", employee.getFullNameFromEmail());
                    employeeNode.put("deposited", employee.getDeposited());
                    employeeNode.put("spent", employee.getSpent());

                    totalDeposited += employee.getDeposited();
                    totalSpent += employee.getSpent();

                    employeesArray.add(employeeNode);
                }
                reportNode.set("employees", employeesArray);

                ArrayNode managersArray = objectMapper.createArrayNode();
                for (User manager : account.getManagers()) {
                    ObjectNode managerNode = objectMapper.createObjectNode();
                    managerNode.put("username", manager.getFullNameFromEmail());
                    managerNode.put("deposited", manager.getDeposited());
                    managerNode.put("spent", manager.getSpent());

                    totalDeposited += manager.getDeposited();
                    totalSpent += manager.getSpent();

                    managersArray.add(managerNode);
                }
                reportNode.set("managers", managersArray);

                reportNode.put("total deposited", totalDeposited);
                reportNode.put("total spent", totalSpent);

                ObjectNode commandNode = objectMapper.createObjectNode();
                commandNode.put("command", "businessReport");
                commandNode.put("timestamp", commandInput.getTimestamp());
                commandNode.set("output", reportNode);

                output.add(commandNode);
                return;
            }
        }
    }

}
