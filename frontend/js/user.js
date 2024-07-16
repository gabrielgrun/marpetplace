document.addEventListener("DOMContentLoaded", init);

function init() {
    bindCadastroAnuncio();
    bindHome();
}

function bindCadastroAnuncio(){
    const minhaArea = document.getElementById("minhaArea");
    if(minhaArea){
        minhaArea.addEventListener("click", function () {
            let textToReplace = window.location.href.substring(window.location.href.lastIndexOf('/'), window.location.href.length);
            window.location.href = window.location.href.replace(textToReplace, '/cadastro-anuncio.html');
        });
    }
}

function bindHome(){
    const home = document.getElementById("home");
    home.addEventListener("click", function () {
        let textToReplace = window.location.href.substring(window.location.href.lastIndexOf('/'), window.location.href.length);
        window.location.href = window.location.href.replace(textToReplace, '/index.html');
    });
}