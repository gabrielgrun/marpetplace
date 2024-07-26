import APIClient from '../../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadAtivos();
}

async function loadAtivos() {
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const data = await apiClient.get('/api/common/anuncios/ativos');
    await fillAtivosInfo(data);
    await bindSelects();
}

async function fillAtivosInfo(data){
    let containerAtivos = document.querySelector('#container-ativos');
    let html;
    html = data.map(anuncio => {
        return `
        <a href="/admin/anuncios.html?id=${anuncio.id}">
        <div id="${anuncio.id}" class="container-fluid d-flex animal-card">
                    <div class="d-flex container-foto">
                        <img class="foto minor-foto"
                            src="data:image/jpeg;base64,${anuncio.foto}"
                            alt="">
                    </div>
                    <div class="d-flex flex-column justify-content-center infos">
                        <div class="d-flex justify-content-center align-items-center">
                            <h1 title="${anuncio.nome}">${anuncio.nome}</h1>
                        </div>
                        <p title="${normalizeRaca(anuncio.raca)}">Raça: ${normalizeRaca(anuncio.raca)}</p>
                        <p>Porte: ${anuncio.porte}</p>
                        <p>${anuncio.sexo === 'M' ? 'Macho' : 'Fêmea'}</p>
                    </div>
                </div>
                </a>`
    }).join('');

    containerAtivos.innerHTML = html;
}

async function bindSelects(){
    const selects = document.querySelectorAll('select');
    selects.forEach(select =>{
        select.addEventListener('change', applyFilter);
    });
}

async function applyFilter(){
    const token = localStorage.getItem('adminToken');
    const apiClient = new APIClient(token);
    const tipo = document.querySelector('#tipo').value;
    const raca = document.querySelector('#raca').value;
    const porte = document.querySelector('#porte').value;
    
    const params = new URLSearchParams();

    if (tipo) params.append('tipo', tipo);
    if (raca) params.append('raca', raca);
    if (porte) params.append('porte', porte);

    const queryString = params.toString();
    const data = await apiClient.get(`/api/common/anuncios/ativos?${queryString}`);
    fillAtivosInfo(data);
}

function normalizeRaca(raca){
    raca = raca.replace(/_/g, '-');
    return raca.charAt(0).toUpperCase() + raca.slice(1).toLowerCase();
}
