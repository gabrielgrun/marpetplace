document.addEventListener("DOMContentLoaded", init);

function init() {
    bindCadastroAnuncio();
    bindHome();
}

function bindCadastroAnuncio(){
    const minhaArea = document.getElementById("minhaArea");
    if(minhaArea){
        minhaArea.addEventListener("click", function () {
            const origin = window.location.origin;
            window.location.href = `${origin}/cadastro-anuncio.html`;
        });
    }
}

function bindHome(){
    const home = document.getElementById("home");
    home.addEventListener("click", function () {
        const origin = window.location.origin;
        window.location.href = `${origin}/index.html`;
    });
}