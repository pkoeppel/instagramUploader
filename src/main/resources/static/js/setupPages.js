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

function checkPicture(source) {
    let reader = new FileReader();
    reader.readAsDataURL(source.files[0]);
    reader.onload = function (e) {
        let image = new Image();
        image.src = e.target.result;
        image.onload = function () {
            if ((this.height === 1365) || (this.width === 1365)) {
                return true;
            }
            alert("Hochgeladene Datei entspricht nicht den vorgegeben Werten (1365 x 1365)!");
            document.getElementById("playerPic").value = "";
            return false;
        };
    };
}

function changedType(selectedType) {
    if (selectedType === "kidsMatch") {
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