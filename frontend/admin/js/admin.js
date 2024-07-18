document.addEventListener("DOMContentLoaded", init);

function init() {
    bindHome();
    bindDenuncias();
    bindDesativados();
    bindDenunciados();
}

function bindHome(){
    const home = document.getElementById("home");
    home.addEventListener("click", function () {
        const origin = window.location.origin;
        window.location.href = `${origin}/admin/ativos.html`;
    });
}

function bindDenuncias(){
    const denuncias = document.getElementById("denuncias");
    denuncias.addEventListener("click", function () {
        const origin = window.location.origin;
        window.location.href = `${origin}/admin/denuncias.html`;
    });
}

function bindDenunciados(){
    const denunciados = document.getElementById("denunciados");
    denunciados.addEventListener("click", function () {
        const origin = window.location.origin;
        window.location.href = `${origin}/admin/denunciados.html`;
    });
}

function bindDesativados(){
    const desativados = document.getElementById("desativados");
    desativados.addEventListener("click", function () {
        const origin = window.location.origin;
        window.location.href = `${origin}/admin/desativados.html`;
    });
}