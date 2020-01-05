angular.module('exercise', [])
    .run(function ($rootScope, $http) {

        $rootScope.getTournaments = function () {
            $http.get('/tournaments').then(function (result) {
                $rootScope.tournaments = result.data;
                angular.forEach($rootScope.tournaments, function (tournament) {
                    $rootScope.getPlayersInTournament(tournament.id);
                });
            });
        };

        $rootScope.addTournament = function (rewardAmount) {
            $http.post('/tournaments/', {
                rewardAmount: rewardAmount
            }).then(function (result) {
                $rootScope.rewardAmount = undefined;
                $rootScope.getTournaments();
            });
        };

        $rootScope.editTournament = function (tournament) {
            $rootScope.editTournamentId = tournament.id;
            $rootScope.editRewardAmount = tournament.rewardAmount;
        };

        $rootScope.updateTournament = function (tournamentId, rewardAmount) {
            $http.put('/tournaments/' + tournamentId, {
                rewardAmount: rewardAmount
            }).then(function (result) {
                $rootScope.editTournamentId = undefined;
                $rootScope.editRewardAmount = undefined;
                $rootScope.getTournaments();
            });
        };

        $rootScope.removeTournament = function (tournamentId) {
            $http.delete('/tournaments/' + tournamentId).then(function (result) {
                $rootScope.getTournaments();
            });
        };

        $rootScope.addPlayerIntoTournament = function (tournamentId, playerId) {
            $http.post('/tournaments/' + tournamentId + '/players/' + playerId, {}).then(function (result) {
                $rootScope.playerTournamentId = undefined;
                $rootScope.playerId = undefined;
                $rootScope.getPlayersInTournament(tournamentId);
            });
        };

        $rootScope.removePlayerFromTournament = function (tournamentId, playerId) {
            $http.delete('/tournaments/' + tournamentId + '/players/' + playerId).then(function (result) {
                $rootScope.getPlayersInTournament(tournamentId);
            });
        };

        $rootScope.addPlayer = function (playerName) {
            $http.post('/players/', {
                name: playerName
            }).then(function (result) {
                $rootScope.playerName = undefined;
                $rootScope.getTournaments();
            });
        };

        $rootScope.getPlayersInTournament = function (tournamentId) {
            $http.get('/tournaments/' + tournamentId + '/players').then(function (result) {
                var index = $rootScope.tournaments.findIndex(function (element) {
                    return element.id == tournamentId;
                });
                $rootScope.tournaments[index].players = result.data;
            });
        };

        $rootScope.getTournaments();
    });