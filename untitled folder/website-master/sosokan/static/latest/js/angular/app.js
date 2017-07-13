var app = angular.module("Sosokan", ['restangular']).config(function($interpolateProvider,
    RestangularProvider, $httpProvider){

    $interpolateProvider.startSymbol("[[");
    $interpolateProvider.endSymbol("]]");
    // cfpLoadingBarProvider.includeSpinner = true;
    // cfpLoadingBarProvider.includeBar = true;

    // api base url
    // https://github.com/mgonto/restangular#setbaseurl
    RestangularProvider.setBaseUrl('');

    // This function is used to map the JSON data to something Restangular
    // expects
    RestangularProvider.setResponseExtractor(function(response, operation, what, url) {
        if (operation === "getList") {
            // Use results as the return type, and save the result metadata
            // in _resultmeta
            var newResponse = response.results;
            newResponse._resultmeta = {
                "count": response.count,
                "next": response.next,
                "previous": response.previous
            };
            return newResponse;
        }

        return response;
    });

    $httpProvider.defaults.xsrfHeaderName = "X-CSRFToken";
    $httpProvider.defaults.xsrfCookieName = 'csrftoken';


});



app.controller("AdsCtrl", function($scope, Restangular) {

    var resource = Restangular.all('api/categories/');
    resource.getList().then(function(categories){
        $scope.categories = categories;
    });


    $scope.loadMore = function(){

        console.log($scope.next);

        if($scope.next){

            var resource = Restangular.all('/'+$scope.next);
            resource.getList().then(function(ads){
                $scope.ads = $scope.ads.concat(ads);
                $scope.next = ads._resultmeta.next;
            });

        }

    }



});