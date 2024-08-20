import APIClient from '../../js/APIClient.js';
import Utils from '../../js/Utils.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadDenuncias();
}

async function loadDenuncias() {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/admin/denuncias/list-all');
    await fillDenunciasInfo(data);
    await bindBtns();
}

async function fillDenunciasInfo(data) {
    let html = '';
    const containerDenuncias = document.querySelector('.container-denuncias');
    html += data.map(current => {
        return `<div class="denuncias-anuncio-group d-flex align-items-center justify-content-between"
                    data-bs-toggle="collapse" href="#collapse-${current.anuncio.id}" aria-expanded="false">
                    <div class="d-flex align-items-center">
                        <h2>${current.anuncio.nome}</h2>
                        <a href="/admin/anuncios.html?id=${current.anuncio.id}">
                            <i id="${current.anuncio.id}" title="Acessar anúncio" class="fa-solid fa-arrow-up-right-from-square"></i>
                        </a>
                    </div>
                    <i class="fa-solid fa-chevron-down"></i>
                </div>
                <div id="collapse-${current.anuncio.id}" class="denuncias-group collapse">
                    ${fillEveryDenuncia(current.denuncias)}
                </div>
                `
    }).join('');

    containerDenuncias.innerHTML = html;
}

function fillEveryDenuncia(denuncias) {
    return denuncias.map(denuncia => {
        return `
                    <div class="card-recurso card-denuncia container-fluid d-flex flex-column">
                        <p>${denuncia.motivo}</p>
                        <div class="btn-group-recurso container-fluid d-flex justify-content-evenly">
                            <button data-id-denuncia="${denuncia.id}" type="button" class="btn-desativar">Recusar</button>
                            <button data-id-denuncia="${denuncia.id}" type="button" class="btn btn-primary btn-reativar">Aceitar</button>
                        </div>
                    </div>
                `
    }).join('');
}

async function bindBtns(){
    const btnAccept = document.querySelectorAll('.btn-reativar');
    const btnRefuse = document.querySelectorAll('.btn-desativar');

    btnAccept.forEach(btn => {
        btn.addEventListener('click', acceptDenuncia);
    })

    btnRefuse.forEach(btn => {
        btn.addEventListener('click', refuseDenuncia);
    })
}

async function acceptDenuncia(e){
    const idDenuncia = e.target.dataset.idDenuncia;
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    await apiClient.patch(`/api/admin/denuncias/aceitar/${idDenuncia}`);
    Utils.showAlert('Denúncia aceita!', 'success');
    await loadDenuncias();
}

async function refuseDenuncia(e){
    const idDenuncia = e.target.dataset.idDenuncia;
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    await apiClient.delete(`/api/admin/denuncias/${idDenuncia}`);
    Utils.showAlert('Denúncia recusada!', 'success');
    await loadDenuncias();
}