import APIClient from '../../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadDenuncias();
}

async function loadDenuncias() {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/admin/denuncias/list-all');
    await fillDenunciasInfo(data);
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
                    <i class="fa-solid fa-chevron-up"></i>
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
                            <button idDenuncia="${denuncia.id}" type="button" class="btn-desativar">Recusar</button>
                            <button idDenuncia="${denuncia.id}" type="button" class="btn btn-primary btn-reativar">Aceitar</button>
                        </div>
                    </div>
                `
    }).join('');
}