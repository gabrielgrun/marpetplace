import APIClient from '../../js/APIClient.js';
import Utils from '../../js/Utils.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadDesativados();
}

async function loadDesativados() {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/admin/usuarios/inativos');
    await fillDesativadosInfo(data);
    bindBtnReativar();
}

async function fillDesativadosInfo(data) {
    const tableAdmin = document.querySelector(".table-admin");
    tableAdmin.innerHTML = `
    <div class="d-flex align-items-center justify-content-between">
                <h2>Nome</h2>
                <h2>E-mail</h2>
                <h2></h2>
            </div>
            ${fillData(data)}
    `

}

function fillData(data) {
    let html = '';
    for (let i = 0; i < data.length; i++) {
        const row = data[i];
        html += `<div class="d-flex align-items-center justify-content-between">
                <p>${row.nome}</p>
                <p>${row.email}</p>
                <button id='${row.id}' type="button" class="btn btn-primary btn-reativar">Reativar</button>    
         </div>`
    }
    return html;
}

function bindBtnReativar() {
    let elements = document.querySelectorAll('.btn-reativar');
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        element.addEventListener('click', reactivateUser);
    }
}

async function reactivateUser(e) {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const id = e.target.id;
    await apiClient.patch(`/api/admin/usuarios/${id}/ativar`);
    Utils.showAlert('Usuário reativado!', 'success');
    loadDesativados();
}