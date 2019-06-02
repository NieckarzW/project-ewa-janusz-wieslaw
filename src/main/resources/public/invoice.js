var app = angular.module('Invoices', []);

app.controller('InvoiceController', function($scope, $http, $window) {
    getAllInvoices()
    $scope.invoiceNumberToSearch = "";
    $scope.newInvoice = getNewInvoice();

    $scope.addInvoice = function(company){
           $http.post('http://localhost:9090/invoices/company', $scope.newCompany);
     }

//    $scope.addEntry = function(entry){
//    $scope.newInvoice.
//    }

    $scope.searchByNumber = function() {
        $http.get('http://localhost:9090/invoices/byNumber?number='+ $scope.invoiceNumberToSearch).then(function(response) {
                    $scope.invoices = [response.data]
                   })
    }

    $scope.clearSearchResult = function() {
        getAllInvoices();
        $scope.invoiceNumberToSearch = "";
        }

    $scope.updateInvoice = function(company){
                   $http.get('http://localhost:9090/invoices/company').then(function() {
                   getNewCompany()
                   })
     }

    $scope.remove = function(id){
        $http.delete('http://localhost:9090/invoices/' + id).then(function() {
            getAllInvoices()
        })
    }

        $scope.removeAll = function(){
            $http.delete('http://localhost:9090/invoices/').then(function() {
                getAllInvoices()
            })
        }

    $scope.pdf = function(id){
        $window.open('http://localhost:9090/invoices/pdf/' + id, '_blank')
     }

     function getNewInvoice() {
     var invoice = new Object();
                 invoice.number = '';
                 invoice.issueDate = '';
                 invoice.dueDate= '';
                 invoice.seller = getNewCompany();
                 invoice.buyer = getNewCompany();
                 invoice.entries = [];
                return invoice;
     }

     function getNewCompany(){
        var company = new Object();
            company.name = '';
            company.address = '';
            company.taxId = '';
            company.accountNumber = "";
            company.phoneNumber = '';
            company.email = '';
           return company;
     }


    function getAllInvoices()
    {
        $http.get('http://localhost:9090/invoices').
            then(function(response) {
                $scope.invoices = response.data;
            });
    }

//    function getInvoiceEntries()
//        {
//            $http.get('http://localhost:9090/invoices').
//                then(function(response) {
//                    $scope.entries = response.data;
//                });
//        }

});
