var app = angular.module('Invoices', []);

app.controller('InvoiceController', function($scope, $http) {
    getAllInvoices()

    $scope.remove = function(id){
        $http.delete('http://localhost:9090/invoices/' + id).then(function() {
            getAllInvoices()
        })
    }

    function getAllInvoices()
    {
        $http.get('http://localhost:9090/invoices').
            then(function(response) {
                $scope.invoices = response.data;
            });
    }
});
