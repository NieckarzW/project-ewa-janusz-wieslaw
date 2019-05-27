var app = angular.module('Invoices', []);

app.controller('InvoiceController', function($scope, $http, $window) {
    getAllInvoices()
    $scope.newCompany = getNewCompany();

/*       <li class="list-group-item">{{newInvoice.buyer.name}}</li>
                                        <li class="list-group-item">{{newInvoice.buyer.address}}</li>
                                        <li class="list-group-item">{{newInvoice.buyer.taxId}}</li>
                                        <li class="list-group-item">{{newInvoice.buyer.accountNumber}}</li>
                                        <li class="list-group-item">{{newInvoice.buyer.phoneNumber}}</li>
                                        <li class="list-group-item">{{newInvoice.buyer.email}}</li>*/

    $scope.addInvoice = function(company){
           $http.post('http://localhost:9090/invoices/company', $scope.newCompany);
        }

    $scope.remove = function(id){
        $http.delete('http://localhost:9090/invoices/' + id).then(function() {
            getAllInvoices()
        })
    }

    $scope.pdf = function(id){
        $window.open('http://localhost:9090/invoices/pdf/' + id, '_blank')
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
});
