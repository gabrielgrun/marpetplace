document.addEventListener("DOMContentLoaded", init);

function init() {
    bindAtivos();
    bindDenuncias();
    bindDesativados();
    bindDenunciados();
}

function bindAtivos(){
    const ativos = document.getElementById("ativos");
    ativos.addEventListener("click", function () {
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