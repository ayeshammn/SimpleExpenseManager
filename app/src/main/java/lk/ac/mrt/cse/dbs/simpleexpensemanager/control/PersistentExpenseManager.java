package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.account_database.Account_database_helper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implement.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.implement.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;


public class PersistentExpenseManager extends ExpenseManager {

    private final Account_database_helper dbHelper;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.dbHelper = new Account_database_helper(context);
        setup();
    }

    @Override
    public void setup()  {

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAO);
        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(persistentAccountDAO);

        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);

        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);
    }
}
