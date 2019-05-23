var app = angular.module('Invoices', []);

app.controller('InvoiceController', function($scope, $http) {
    getAllInvoices()

    $scope.remove = function(id){
        $http.delete('http://localhost:9090/invoices/' + id).then(function() {
            getAllInvoices()
        })
    }

//    function downloadPDF()
//    {
//    $http.get(baseUrl + apiUrl, { responseType: 'arraybuffer' })
//          .success(function (response) {
//             var file = new Blob([response], { type: 'application/pdf' });
//             var fileURL = URL.createObjectURL(file);
//             $scope.pdfContent = $sce.trustAsResourceUrl(fileURL);
//           })
//           .error(function () {
//           });
//             }

    function getAllInvoices()
    {
        $http.get('http://localhost:9090/invoices').
            then(function(response) {
                $scope.invoices = response.data;
            });
    }
});
