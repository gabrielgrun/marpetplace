import APIClient from './APIClient.js';
import Jwt from './Jwt.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadAtivos();
}

async function loadAtivos() {
    const token = localStorage.getItem('userToken');
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
        <a href="/anuncios.html?id=${anuncio.id}">
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
                    <i class="fa-solid fa-circle-exclamation danger" data-anuncio-id="${anuncio.id}" data-bs-toggle="modal" data-bs-target="#denunciaModal"></i>
                </div>
                </a>`
    }).join('');

    containerAtivos.innerHTML = html;

    bindDenunciaBtn();
}

function bindDenunciaBtn(){
    document.querySelectorAll('.danger').forEach(icon => {
        icon.addEventListener('click', function(event) {
            event.stopPropagation();
            event.preventDefault();

            const anuncioId = event.target.getAttribute('data-anuncio-id');
            document.getElementById('denunciaModal').setAttribute('data-anuncio-id', anuncioId);
        });
    });

    bindSaveBtn();
}

async function bindSaveBtn(){
    document.getElementById('btnSave').addEventListener('click', async function() {
        const modalElement = document.getElementById('denunciaModal');
        const anuncioId = modalElement.getAttribute('data-anuncio-id');
        const inputMotivo = modalElement.querySelector('#inputMotivo');
        const motivo = inputMotivo.value.trim();

        if (!motivo) {
            return inputMotivo.classList.add('is-invalid');
        } else {
            inputMotivo.classList.remove('is-invalid');
        }
        
        await saveDenuncia(motivo, anuncioId);
        
        inputMotivo.value = '';
        const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
        modalInstance.hide();
    });
}

async function saveDenuncia(motivo, anuncioId){
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const jwt = new Jwt();
    const idDenunciante = jwt.parseJwt(token).id;
    const json = {
        motivo: motivo,
        idAnuncio: anuncioId,
        idDenunciante: idDenunciante
    }

    await apiClient.post('/api/usuarios/denuncias', json);
}

async function bindSelects(){
    const selects = document.querySelectorAll('select');
    selects.forEach(select =>{
        select.addEventListener('change', applyFilter);
    });
}

async function applyFilter(){
    const token = localStorage.getItem('userToken');
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
