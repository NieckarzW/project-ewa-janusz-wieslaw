angular.module('demo', [])
.controller('Invoice', function($scope, $http) {
    $http.get('http://localhost:8080/invoices').
        then(function(response) {
            $scope.invoices = response.data;
        });
});