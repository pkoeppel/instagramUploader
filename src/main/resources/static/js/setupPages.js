function fillSelect() {
    let sel = document.getElementById("opponent");
    fetch('http://localhost:8080/getAllTeams')
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            data.forEach(element => {
                let opt = document.createElement("option");
                opt.text = element;
                sel.append(opt);
            });
        })
}

function fillDateAndTime() {
    let nextSun = new Date();
    nextSun.setDate(nextSun.getDate() + (7 - nextSun.getDay()) % 7);

    let day = nextSun.getDate();
    if (day < 10) {
        day = "0" + day;
    }
    let month = nextSun.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }

    document.getElementById("kickoffDate").value = nextSun.getFullYear() + "-" + month + "-" + day;

    document.getElementById("kickoffTime").value = "15:00";
}

function loadPage() {
    fillSelect();
    fillDateAndTime();
}



function changedType(selectedType) {
    if (selectedType === "youthMatch") {
        document.getElementById("lbKickoffTime").style.visibility = "hidden";
    } else {
        document.getElementById("lbKickoffTime").style.visibility = "visible";
    }
}

function changeHome(sel) {
    if (sel.checked) {
        document.getElementById("home").style.visibility = "visible";
    } else {
        document.getElementById("home").style.visibility = "hidden";
    }
}

function clearFields() {
    document.getElementById("oppName").value = "";
}

function loadMenMatches(type) {
    let sel = document.getElementById("matches");
    let url = 'http://localhost:8080/getAllMenMatches'
    fetch(url, {
        method: 'POST',
        body: type
    })
        .then((result) => result.json())
        .then((data) => {
            data.forEach(element => {
                let match = element.game;
                let type = match.matchType;
                let opp = match.opponent;
                let date = match.matchDate;
                let opt = document.createElement("option");
                opt.text = date + ", " + opp + ", " + type;
                opt.value = JSON.stringify(match);
                sel.append(opt);
            });
        });
}