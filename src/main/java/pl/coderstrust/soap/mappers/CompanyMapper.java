package pl.coderstrust.soap.mappers;


import pl.coderstrust.soap.bindingclasses.Company;

public class CompanyMapper {

  public static Company mapCompany(pl.coderstrust.model.Company company) {
    Company mappedCompany = new Company();
    mappedCompany.setId(company.getId());
    mappedCompany.setName(company.getName());
    mappedCompany.setAddress(company.getAddress());
    mappedCompany.setTaxId(company.getTaxId());
    mappedCompany.setAccountNumber(company.getAccountNumber());
    mappedCompany.setPhoneNumber(company.getPhoneNumber());
    mappedCompany.setEmail(company.getEmail());
    return mappedCompany;
  }

  public static pl.coderstrust.model.Company mapCompany(Company company) {
    pl.coderstrust.model.Company mappedCompany = new pl.coderstrust.model.Company(
        Long.valueOf(company.getId()),
        company.getName(),
        company.getAddress(),
        company.getTaxId(),
        company.getAccountNumber(),
        company.getPhoneNumber(),
        company.getEmail());
    return mappedCompany;
  }

}
