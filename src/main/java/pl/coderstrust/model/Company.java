package pl.coderstrust.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final Long id;
  @ApiModelProperty(value = "Company Name", example = "Coca-Cola")
  private final String name;
  @ApiModelProperty(value = "Company Address", example = "Wersalska 47/75, 91-203 Łódź")
  private final String address;
  @ApiModelProperty(value = "Tax Id", example = "524-21-06-963")
  private final String taxId;
  @ApiModelProperty(value = "Account number", example = "PL 27 1140 2004 0000 3002 0135 5387")
  private final String accountNumber;
  @ApiModelProperty(value = "Phone number", example = "(22) 519-55-55")
  private final String phoneNumber;
  @ApiModelProperty(value = "email address", example = "cocacola@coke.com")
  private final String email;

  @JsonCreator
  public Company(@JsonProperty("id") Long id,
                 @JsonProperty("name") String name,
                 @JsonProperty("address") String address,
                 @JsonProperty("taxId") String taxId,
                 @JsonProperty("accountNumber") String accountNumber,
                 @JsonProperty("phoneNumber") String phoneNumber,
                 @JsonProperty("email") String email) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.taxId = taxId;
    this.accountNumber = accountNumber;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }

  private Company() {
    id = null;
    name = null;
    address = null;
    taxId = null;
    accountNumber = null;
    phoneNumber = null;
    email = null;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public String getTaxId() {
    return taxId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    return Objects.equals(id, company.id)
        && Objects.equals(name, company.name)
        && Objects.equals(address, company.address)
        && Objects.equals(taxId, company.taxId)
        && Objects.equals(accountNumber, company.accountNumber)
        && Objects.equals(phoneNumber, company.phoneNumber)
        && Objects.equals(email, company.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, address, taxId, accountNumber, phoneNumber, email);
  }

  @Override
  public String toString() {
    return "Company{"
        + "id='" + id + '\''
        + ", name='" + name + '\''
        + ", address='" + address + '\''
        + ", taxId='" + taxId + '\''
        + ", accountNumber='" + accountNumber + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + ", email='" + email + '\''
        + '}';
  }
}
