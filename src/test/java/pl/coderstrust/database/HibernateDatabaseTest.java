package pl.coderstrust.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderstrust.service.InvoiceService;

@ExtendWith(MockitoExtension.class)
class HibernateDatabaseTest {

  @Mock
  private HibernateInvoiceRepository repository;

  @InjectMocks
  private  HibernateDatabase hibernateDatabase;

  @Test
  void saveInvoiceShouldThrowIllegalArgumentExceptionForNullAsInvoice() {
    assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.saveInvoice(null));
  }

  @Test
  void deleteInvoiceMethodShouldThrowIllegalArgumentExceptionForNullAsId() {
    assertThrows(IllegalArgumentException.class, () -> hibernateDatabase.deleteInvoice(null));
  }
}