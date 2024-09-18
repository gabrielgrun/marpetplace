import APIClient from '../../js/APIClient.js';
import Utils from '../../js/Utils.js';

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
    <table>
    <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Qt. denúncias</th>
                <th></th>
    </tr>        
            ${fillData(data)}
    `
}

function fillData(data){
    let html = '';
    for (let i = 0; i < data.length; i++) {
        const row = data[i];
            html += `<tr>
            <td>${row.nome}</td>
            <td>${row.email}</td>
            <td>${row.numeroDenuncias}</td>
            <td><button id='${row.id}' type="button" class="btn-desativar">Desativar</button> </td>   
            </tr>
        `
    }
    html += `</table>`;
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
    Utils.showAlert('Usuário desativado!', 'success');
    loadDenunciados();
}