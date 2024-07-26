import APIClient from '../../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadDenunciados();
}

async function loadDenunciados(){
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/admin/usuarios/mais-denunciados');
    await fillDenunciadosInfo(data);
    bindBtnDesativar();
}

async function fillDenunciadosInfo(data){
    const tableAdmin = document.querySelector(".table-admin");
    tableAdmin.innerHTML = `
    <div class="d-flex align-items-center justify-content-between">
                <h2>Nome</h2>
                <h2>E-mail</h2>
                <h2>Qt. denúncias</h2>
                <h2></h2>
            </div>
            ${fillData(data)}
    `
}

function fillData(data){
    let html = '';
    for (let i = 0; i < data.length; i++) {
        const row = data[i];
        html += `<div class="d-flex align-items-center justify-content-between">
                <p>${row.nome}</p>
                <p>${row.email}</p>
                <p>${row.numeroDenuncias}</p>
                <button id='${row.id}' type="button" class="btn-desativar">Desativar</button>    
            </div>`
    }
    return html;
}

function bindBtnDesativar(){
    let elements = document.querySelectorAll('.btn-desativar');
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        element.addEventListener('click', inactivateUser);
    }
}

async function inactivateUser(e){
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const id = e.target.id;
    await apiClient.delete(`/api/admin/usuarios/${id}`);
    loadDenunciados();
}