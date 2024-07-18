import { fetchGetRequest, fetchPatchRequest } from '../../js/requests.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadDesativados();
}

async function loadDesativados(){
    const data = await fetchGetRequest('/api/admin/usuarios/inativos', localStorage.getItem('adminToken'));
    await fillDesativadosInfo(data);
    bindBtnReativar();
}

async function fillDesativadosInfo(data){
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

function fillData(data){
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

function bindBtnReativar(){
    let elements = document.querySelectorAll('.btn-reativar');
    for (let i = 0; i < elements.length; i++) {
        const element = elements[i];
        element.addEventListener('click', reactivateUser);
    }
}

async function reactivateUser(e){
    const id = e.target.id;
    await fetchPatchRequest(`/api/admin/usuarios/${id}/ativar`, localStorage.getItem('adminToken'));
    loadDesativados();
}