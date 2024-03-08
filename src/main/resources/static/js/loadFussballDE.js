/*
 * fussball.de widgetAPI
 */

const egmWidget2 = {};

egmWidget2.url = '//www.fussball.de/widget2';

egmWidget2.referer = '';
if (location.host) {
    egmWidget2.referer = encodeURIComponent(location.host);
} else {
    egmWidget2.referer = 'unknown';
}

fussballdeWidgetAPI = function () {
    let D = {};

    D.showWidget = function (E, K) {
        if (K !== undefined && K !== "" && E !== undefined && E !== "") {
            if (document.getElementById(E)) {
                if (K !== "") {
                    let src = egmWidget2.url + "/-"
                        + "/schluessel/" + K
                        + "/target/" + E
                        + "/caller/" + egmWidget2.referer;
                    createIFrame(E, src);
                }
            } else {
                alert("Der angegebene DIV mit der ID " + E
                    + " zur Ausgabe existiert nicht.")
            }
        }
    };

    window.addEventListener("message", receiveMessage, false);

    function receiveMessage(event) {
        if (event.data.type === 'setHeight') {
            document.querySelectorAll('#' + event.data.container + ' iframe')[0].setAttribute('height', '100%');
        }

        if (event.data.type === 'setWidth') {
            document.querySelectorAll('#' + event.data.container + ' iframe')[0].setAttribute('width', '100%');
        }

        if (event.data.type === 'setScrolling') {
            document.querySelectorAll('#' + event.data.container + ' iframe')[0].setAttribute('scrolling', 'yes');
        }
    }

    return D
};


function createIFrame(parentId, src) {
    let parent = document.getElementById(parentId);
    let iframe = document.createElement('iframe');
    iframe.setAttribute("src", src);
    iframe.setAttribute("style", "border: 1px solid #CECECE;");
    parent.innerHTML = "";
    parent.appendChild(iframe);
}
