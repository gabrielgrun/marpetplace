import APIClient from '../../js/APIClient.js';
import Utils from '../../js/Utils.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadRecursos();
}

async function loadRecursos() {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/admin/recursos/list-all');
    await fillRecursosInfo(data);
    await bindBtns();
}

async function fillRecursosInfo(data) {
    let html = '';
    const containerRecursos = document.querySelector('.grid-recurso');
    html += data.map(current => {
        return `<div class="card-recurso container-fluid d-flex flex-column">
                    <a href="/admin/anuncios.html?id=${current.idAnuncio}">
                        <i title="Acessar anúncio" class="fa-solid fa-arrow-up-right-from-square"></i>
                    </a>
                    <p>${current.justificativa}</p>
                    <div class="btn-group-recurso container-fluid d-flex justify-content-evenly">
                        <button data-id-recurso="${current.id}" type="button" class="btn-desativar">Recusar</button>
                        <button data-id-recurso="${current.id}" type="button" class="btn btn-primary btn-reativar">Aceitar</button>
                    </div>
                </div>
                `
    }).join('');

    containerRecursos.innerHTML = html;
}

async function bindBtns(){
    const btnAccept = document.querySelectorAll('.btn-reativar');
    const btnRefuse = document.querySelectorAll('.btn-desativar');

    btnAccept.forEach(btn => {
        btn.addEventListener('click', acceptRecurso);
    })

    btnRefuse.forEach(btn => {
        btn.addEventListener('click', refuseRecurso);
    })
}

async function acceptRecurso(e){
    const idRecurso = e.target.dataset.idRecurso;
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    await apiClient.patch(`/api/admin/recursos/aceitar/${idRecurso}`);
    Utils.showAlert('Recurso aceito!', 'success');
    await loadRecursos();
}

async function refuseRecurso(e){
    const idRecurso = e.target.dataset.idRecurso;
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    await apiClient.delete(`/api/admin/recursos/${idRecurso}`);
    Utils.showAlert('Recurso recusado!', 'success');
    await loadRecursos();
}