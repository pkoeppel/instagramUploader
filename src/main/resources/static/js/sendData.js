function sendLeagueMatch() {
    if (checkInputs()) {
        let ownPoints = document.querySelector("#pointsSelf").value;
        let ownPlace = document.querySelector("#placeSelf").value;
        let ownGoals = document.querySelector("#goalsSelf").value;
        let ownAgainstGoals = document.querySelector("#goalsAgainstSelf").value;
        let ownForm = document.querySelector("#formSelf").value;
        let oppPoints = document.querySelector("#pointsOpponent").value;
        let oppPlace = document.querySelector("#placeOpponent").value;
        let oppGoals = document.querySelector("#goalsOpponent").value;
        let oppAgainstGoals = document.querySelector("#goalsAgainstOpponent").value;
        let oppForm = document.querySelector("#formOpponent").value;

        let ownStats = "\nPlatz " + ownPlace + " (" + ownPoints + " / " + ownGoals + ":" + ownAgainstGoals + ")\nTrend: " + ownForm;
        let oppStats = "\nPlatz " + oppPlace + " (" + oppPoints + " / " + oppGoals + ":" + oppAgainstGoals + ")\nTrend: " + oppForm;

        let data = {
            team: "men",
            matchType: "leagueMatch",
            matchDay: document.getElementById('matchday').value,
            homeGame: document.getElementById('homeGame').checked,
            ownStats: ownStats,
            opponent: document.getElementById('opponent').value,
            oppStats: oppStats,
            matchDate: document.getElementById('kickoffDate').value,
            matchTime: document.getElementById('kickoffTime').value,
            homePlace: null,
            oppName: null
        };
        fetch('http://localhost:8080/createLeagueMatchFile', {
            method: 'POST',
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {'Content-Type': 'application/json',},
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success: ', data.result);
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    } else {
        alert("Bitte alle Felder ausfüllen");
    }


}

function sendCupMatch() {
    if (checkInputs()) {
        let data = {
            team: "men",
            matchType: "cupMatch",
            matchDay: document.getElementById('cupround').value,
            homeGame: document.getElementById('homeGame').checked,
            ownStats: null,
            opponent: document.getElementById('opponent').value,
            oppStats: null,
            matchDate: document.getElementById('kickoffDate').value,
            matchTime: document.getElementById('kickoffTime').value,
            homePlace: null,
            oppName: null
        };
        fetch('http://localhost:8080/createCupMatchFile', {
            method: 'POST',
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {'Content-Type': 'application/json',},
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success: ', data.result);
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    } else {
        alert("Bitte alle Felder ausfüllen");
    }
}

function sendFriendMatch() {
    if (checkInputs()) {
        let data = {
            team: "men",
            matchType: "friendMatch",
            matchDay: null,
            homeGame: document.getElementById('homeGame').checked,
            ownStats: null,
            opponent: document.getElementById('opponent').value,
            oppStats: null,
            matchDate: document.getElementById('kickoffDate').value,
            matchTime: document.getElementById('kickoffTime').value,
            homePlace: null,
            oppName: null
        };
        fetch('http://localhost:8080/createFriendMatchFile', {
            method: 'POST',
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {'Content-Type': 'application/json',},
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then((data) => {
                console.log('Success: ', data);
                //window.location.assign(file);
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    } else {
        alert("Bitte alle Felder ausfüllen");
    }
}

function sendKickoffData() {
    //send
    if (checkInputs()) {
        let file = document.getElementById('playerPic').files[0];
        let data = {
            match: document.getElementById('matches').value,
            playerPic: file.name
        }
        fetch('http://localhost:8080/createKickoffFile', {
            method: 'POST',
            mode: 'cors',
            cache: 'no-cache',
            credentials: 'same-origin',
            headers: {'Content-Type': 'application/json',},
            redirect: 'follow',
            referrerPolicy: 'no-referrer',
            body: JSON.stringify(data),
        })
            .then(response => response.json())
            .then((data) => {
                console.log('Success: ', data);
            })
            .catch((error) => {
                console.error('Error: ', error);
            });
    } else {
        alert("Bitte alle Felder ausfüllen");
    }
}

function sendKidsMatchday() {
    fetch('http://localhost:8080/createMatchFilesKids', {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {'Content-Type': 'application/json',},
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(matchData),
    })
        .then(response => response.text())
        .then((data) => {
            console.log(JSON.stringify(matchData));
            console.log('Success: ', data);
            //window.location.assign(file);
        })
        .catch((error) => {
            console.error('Error: ', error);
        });

}

function checkInputs() {
    let inputs = document.querySelectorAll('.mainpage input');
    for (let input of inputs) {
        if (input.value === '') {
            return false;
        }
    }
    return true;
}