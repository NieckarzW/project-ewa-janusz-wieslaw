package pl.coderstrust.mappers;


import pl.coderstrust.soap.bindingclasses.Company;

public class CompanyMapper {

  public static Company mapModelCompanyToSoapCompany(pl.coderstrust.model.Company modelCompany) {
    Company soapCompany = new Company();
    soapCompany.setId(modelCompany.getId());
    soapCompany.setName(modelCompany.getName());
    soapCompany.setAddress(modelCompany.getAddress());
    soapCompany.setTaxId(modelCompany.getTaxId());
    soapCompany.setAccountNumber(modelCompany.getAccountNumber());
    soapCompany.setPhoneNumber(modelCompany.getPhoneNumber());
    soapCompany.setEmail(modelCompany.getEmail());
    return soapCompany;
  }

  public static pl.coderstrust.model.Company mapSoapCompanyToModelCompany(Company soapCompany) {
    pl.coderstrust.model.Company modelCompany = new pl.coderstrust.model.Company(
        Long.valueOf(soapCompany.getId()),
        soapCompany.getName(),
        soapCompany.getAddress(),
        soapCompany.getTaxId(),
        soapCompany.getAccountNumber(),
        soapCompany.getPhoneNumber(),
        soapCompany.getEmail());
    return modelCompany;
  }

}
