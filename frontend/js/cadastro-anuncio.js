import Jwt from '../js/Jwt.js';
import APIClient from '../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

const bindedFunctions = {};

function init() {
    loadAnuncios();
    maskInput();
    bindBtnSave();
    bindBtnHideOrShow();
}

async function loadAnuncios() {
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const jwt = new Jwt();
    const usuarioId = jwt.parseJwt(token).id;
    const data = await apiClient.get(`/api/usuarios/${usuarioId}/anuncios`);
    console.log(data);
    await fillAnunciosInfo(data);
}

async function fillAnunciosInfo(data){
    let sideMenuCadastro = document.querySelector('#side-menu-cadastro');
    let html = `<div class="div-btn-novo-anuncio">
                <button class="btn-novo-anuncio">Novo anúncio</button>
            </div>`;
    html += data.map(anuncio => {
        bindedFunctions[anuncio.id] = focusAnuncio.bind(null, anuncio);
        return `
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
                    ${fillStatus(anuncio.anuncioStatus)}                    
                </div>
                `
    }).join('');

    sideMenuCadastro.innerHTML = html;

    bindActiveAnuncio();
}

function bindActiveAnuncio(){
    const cards = document.querySelectorAll('.animal-card');
    cards.forEach(card => card.addEventListener('click', bindedFunctions[card.id]));
}

function focusAnuncio(data, event){
    console.log('anuncio: ', data);
    const cards = document.querySelectorAll('.animal-card');
    cards.forEach(card => {
        card.classList.remove('cadastro-active');
    })

    const anuncio = getAnimalCard(event.target);
    anuncio.classList.add('cadastro-active');
    fillInputs();
}

function getAnimalCard(element){
    if(element.classList.contains('animal-card')){
        return element;
    }

    return getAnimalCard(element.parentNode);
}

function fillInputs(data){
    if(data.status = ocultado){
        
    }
}

function fillStatus(anuncio){
    if(anuncio === 'DENUNCIADO'){
        return `<i class="fa-solid fa-circle-exclamation danger"></i>`;
    }
    
    if(anuncio === 'OCULTADO'){
        return `<i class="fa-solid fa-eye-slash eye-hidden"></i>`;
    }

    return '';
}

function maskInput() {
    const inputContato = document.getElementById("inputContato");

    inputContato.addEventListener("input", function () {
        const phone = phoneMask(this.value);
        this.value = phone;
    });

    inputContato.addEventListener("input", function (e) {
        var x = e.target.value.replace(/\D/g, "").match(/(\d{0,2})(\d{0,5})(\d{0,4})/);
        e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
    });
}

function phoneMask(phone) {
    const mask = "(##) #####-####";
    let str = "";

    for (let i = 0, j = 0; i < phone.length; i++) {
        if (mask.charAt(i) === "#" && phone.charAt(i) !== "") {
            str += phone.charAt(i);
            j++;
        } else if (mask.charAt(i) !== "#" && j < mask.length) {
            str += phone.charAt(i);
            j++;
        }
    }

    return str;
}

function bindBtnSave(){
    document.querySelector('#btn-salvar').addEventListener('click', save);
}

function bindBtnHideOrShow(){
    document.querySelector('#btn-ocultar').addEventListener('click', hideOrShow);
}

async function save(e) {
    e.preventDefault();
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const jwt = new Jwt();
    const usuarioId = jwt.parseJwt(token).id;

    const formData = getFormData();
    //clearFields();
    try {
        const data = await apiClient.post(`/api/usuarios/${usuarioId}/anuncios`, formData);
        console.log('Anuncio enviado com sucesso:', data);
    } catch (error) {
        console.error('Erro ao enviar anuncio:', error);
    }
}

async function hideOrShow(e) {
    e.preventDefault();
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const id = document.querySelector('#inputId').value;

    const data = await apiClient.patch(`/api/usuarios/anuncios/${id}`, formData);
}

function getFormData(){
    const formData = new FormData();
    const nome = document.querySelector('#inputNome').value;
    const descricao = document.querySelector('#inputDescricao').value;
    const foto = document.querySelector('input[type="file"]').files[0];
    const porte = document.querySelector('#selectPorte').value;
    const sexo = document.querySelector('#selectSexo').value;
    const castrado = document.querySelector('#checkboxCastrado').value;
    const vacinado = document.querySelector('#checkboxVacinado').value;
    const contato = document.querySelector('#inputContato').value;
    const tipo = document.querySelector('#selectTipo').value;
    const raca = document.querySelector('#selectRaca').value;
    
    formData.append('nome', nome);
    formData.append('descricao', descricao);
    formData.append('foto', foto);
    formData.append('porte', porte);
    formData.append('sexo', sexo);
    formData.append('castrado', castrado);
    formData.append('vacinado', vacinado);
    formData.append('contato', contato);
    formData.append('tipo', tipo);
    formData.append('raca', raca);
    return formData;
}

function clearFields() {
    const form = document.querySelector('form');
    const inputsAndSelects = form.querySelectorAll('input, select');
    inputsAndSelects.forEach(input => {
        input.value = '';
    })
}

function normalizeRaca(raca){
    raca = raca.replace(/_/g, '-');
    return raca.charAt(0).toUpperCase() + raca.slice(1).toLowerCase();
}