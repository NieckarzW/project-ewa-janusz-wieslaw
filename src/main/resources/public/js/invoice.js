var app = angular.module('Invoices', []);

app.controller('InvoiceController', function ($scope, $http, $window) {

    $scope.getAllInvoices = function () {
        $http.get(getBaseApiAddress()).then(function (response) {
            $scope.invoices = response.data;
        }, function (response) {
            showError(response.data.message);
        });
    }

    $scope.searchByNumber = function (number) {
        $http.get(getBaseApiAddress() + 'byNumber?number=' + number).then(function (response) {
            $scope.invoices = [response.data]
        }, function (response) {
            showError(response.data.message);
        })
    }

    $scope.remove = function (id) {
        $http.delete(getBaseApiAddress() + id).then(function () {
            $scope.getAllInvoices()
        }, function (response) {
            showError(response.data.message);
        })
    }

    $scope.removeAll = function () {
        $http.delete(getBaseApiAddress()).then(function () {
            $scope.getAllInvoices()
        }, function (response) {
            showError(response.data.message);
        })
    }

    $scope.pdf = function (id) {
        $window.open(getBaseApiAddress() + 'pdf/' + id, '_blank')
    }

    function getBaseApiAddress() {
        return 'http://localhost:8080/invoices/'
    }

    function showError(message) {
        $.notify({
                     message: message
                 }, {
                     type: 'danger'
                 });
    }
});
