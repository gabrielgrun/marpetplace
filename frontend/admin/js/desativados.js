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
    <table>
    <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th></th>
            </div>
    </tr>        
            ${fillData(data)}
    `
}

function fillData(data) {
    let html = '';
    for (let i = 0; i < data.length; i++) {
        const row = data[i];
        html += `<tr>
                <td>${row.nome}</td>
                <td>${row.email}</td>
                <td class="d-flex justify-content-end"><button id='${row.id}' type="button" class="btn btn-primary btn-reativar">Reativar</button></td>
         </tr>`
    }
    html += `</table>`;
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