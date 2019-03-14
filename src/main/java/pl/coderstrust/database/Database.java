package pl.coderstrust.database;

interface Database {
  void saveInvoice() throws DatabaseOperationException;

  void deleteInvoice() throws DatabaseOperationException;

  void getInvoice() throws DatabaseOperationException;

  void getAllInvoices() throws DatabaseOperationException;

  void deleteAllInvoices() throws DatabaseOperationException;

  void invoiceExists() throws DatabaseOperationException;

  void countInvoices() throws DatabaseOperationException;
}
