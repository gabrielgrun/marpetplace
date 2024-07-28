import Jwt from '../js/Jwt.js';
import APIClient from '../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

const bindedFunctions = {};

function init() {
    loadAnuncios();
    bindBtnSave();
    maskInput();
}

async function loadAnuncios() {
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const jwt = new Jwt();
    const usuarioId = jwt.parseJwt(token).id;
    const data = await apiClient.get(`/api/usuarios/${usuarioId}/anuncios`);
    await fillAnunciosInfo(data);
}

async function fillAnunciosInfo(data) {
    let sideMenuCadastro = document.querySelector('#side-menu-cadastro');
    let html = `<div class="div-btn-novo-anuncio">
                <button id="novo-anuncio" class="btn-novo-anuncio">Novo anúncio</button>
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
    bindBtnNovoAnuncio();
}

function bindBtnNovoAnuncio() {
    document.querySelector('#novo-anuncio').addEventListener('click', clearFields);
}

function bindActiveAnuncio() {
    const cards = document.querySelectorAll('.animal-card');
    cards.forEach(card => card.addEventListener('click', bindedFunctions[card.id]));
}

function removeAnuncioFocus() {
    const cards = document.querySelectorAll('.animal-card');
    cards.forEach(card => {
        card.classList.remove('cadastro-active');
    })
}

function focusAnuncio(data, event) {
    removeAnuncioFocus();

    const anuncio = getAnimalCard(event.target);
    anuncio.classList.add('cadastro-active');

    if (data.anuncioStatus === 'OCULTADO') {
        buildCadastro(data);
        document.querySelector('#btn-ocultar').innerText = 'Exibir anúncio';
    } else if (data.anuncioStatus === 'ATIVO') {
        buildCadastro(data);
        document.querySelector('#btn-ocultar').innerText = 'Ocultar anúncio';
    } else if (data.anuncioStatus === 'DENUNCIADO') {
        return buildCadastroDenunciado(data);
    }

    fillInputs(data);
}

function buildCadastro(data) {
    document.querySelector('#form-cadastro').innerHTML = `<form class="d-flex">
                <div class="container-fluid">
                    <input id="inputId" type="hidden">
                    <div class="form-group">
                        <label for="inputNome">Nome</label>
                        <input required type="text" class="form-control" id="inputNome">
                    </div>
                    <div class="form-group">
                        <label for="inputContato">Contato</label>
                        <input required type="tel" placeholder="(99) 99999-9999" class="form-control" id="inputContato">
                    </div>
                    <div class="form-group">
                        <label for="selectRaca">Raça</label>
                        <select required id="selectRaca" class="form-select">
                            <option value="" selected></option>
                            <option value="VIRA_LATA">Vira-lata</option>
                            <option value="LABRADOR">Labrador</option>
                            <option value="BULDOGUE_FRANCÊS">Buldogue Francês</option>
                            <option value="PASTOR_ALEMÃO">Pastor Alemão</option>
                            <option value="GOLDEN_RETRIEVER">Golden Retriever</option>
                            <option value="BULDOGUE_INGLÊS">Buldogue Inglês</option>
                            <option value="POODLE">Poodle</option>
                            <option value="BEAGLE">Beagle</option>
                            <option value="ROTTWEILER">Rottweiler</option>
                            <option value="YORKSHIRE_TERRIER">Yorkshire Terrier</option>
                            <option value="PERSA">Persa</option>
                            <option value="SIAMES">Siamês</option>
                            <option value="MAINE_COON">Maine Coon</option>
                            <option value="RAGDOLL">Ragdoll</option>
                            <option value="BENGAL">Bengal</option>
                            <option value="SPHYNX">Sphynx</option>
                            <option value="BRITISH_SHORTHAIR">British Shorthair</option>
                            <option value="SCOTTISH_FOLD">Scottish Fold</option>
                            <option value="SIBERIANO">Siberiano</option>
                            <option value="ABISSINIO">Abissínio</option>
                            <option value="OUTRA">Outra</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="inputDescricao">Descrição</label>
                        <textarea required class="form-control" id="inputDescricao" rows="3"></textarea>
                    </div>
                </div>
                <div class="container-fluid">
                    <div class="form-group">
                        <label class="form-check-label" for="inputFoto">Foto</label>
                        <input type="file" class="form-control" id="inputFoto" aria-label="Upload" accept="image/*">
                    </div>
                    <div class="container-select-cadastro">
                        <label class="form-check-label labelCaracteristicas">Características</label>
                        <div class=" d-flex justify-content-between">
                            <select required id="selectPorte" class="form-select" aria-label="Default select example">
                                <option value="" selected>Porte</option>
                                <option value="P">P</option>
                                <option value="M">M</option>
                                <option value="G">G</option>
                            </select>
                            <select required id="selectTipo" class="form-select" aria-label="Default select example">
                                <option value="" selected>Tipo</option>
                                <option value="GATO">Gato</option>
                                <option value="CACHORRO">Cachorro</option>
                            </select>
                            <select required id="selectSexo" class="form-select" aria-label="Default select example">
                                <option value="" selected>Sexo</option>
                                <option value="M">Macho</option>
                                <option value="F">Fêmea</option>
                            </select>
                        </div>

                    </div>
                    <div class="form-check form-switch">
                        <input type="checkbox" class="form-check-input" role="switch" id="checkboxCastrado">
                        <label class="form-check-label" for="checkboxCastrado">Castrado</label>
                    </div>
                    <div class="form-check form-switch">
                        <input type="checkbox" class="form-check-input" role="switch" id="checkboxVacinado">
                        <label class="form-check-label" for="checkboxVacinado">Vacinado</label>
                    </div>
                    <div class="d-flex justify-content-evenly btn-group">
                        <button id="btn-ocultar" class="btn-cadastro">Ocultar anúncio</button>
                        <button id="btn-salvar" class="btn-cadastro">Salvar</button>
                    </div>
                </div>
            </form>`;

    maskInput();
    bindBtnSave();
    bindBtnHideOrShow(hideOrShow.bind(null, data.anuncioStatus));
}

function buildCadastroDenunciado(data) {
    document.querySelector('#form-cadastro').innerHTML = '';
}

function getAnimalCard(element) {
    if (element.classList.contains('animal-card')) {
        return element;
    }

    return getAnimalCard(element.parentNode);
}

function fillInputs(data) {
    console.log(data);
    document.querySelector('#inputId').value = data.id;
    document.querySelector('#inputNome').value = data.nome;
    document.querySelector('#inputDescricao').value = data.descricao;
    document.querySelector('input[type="file"]').files[0];
    document.querySelector('#selectPorte').value = data.porte;
    document.querySelector('#selectSexo').value = data.sexo;
    document.querySelector('#checkboxCastrado').checked = data.castrado;
    document.querySelector('#checkboxVacinado').checked = data.vacinado;
    document.querySelector('#inputContato').value = data.contato;
    document.querySelector('#selectTipo').value = data.tipo;
    document.querySelector('#selectRaca').value = data.raca;
}

function fillStatus(anuncio) {
    if (anuncio === 'DENUNCIADO') {
        return `<i class="fa-solid fa-circle-exclamation danger"></i>`;
    }

    if (anuncio === 'OCULTADO') {
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

function bindBtnSave() {
    document.querySelector('#btn-salvar').addEventListener('click', save);
}

function bindBtnHideOrShow(f) {
    document.querySelector('#btn-ocultar').addEventListener('click', f);
}

async function save(e) {
    e.preventDefault();
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const jwt = new Jwt();
    const usuarioId = jwt.parseJwt(token).id;
    const formData = getFormData();
    const id = document.querySelector('#inputId').value;
    const form = document.querySelector('form');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    if (id) {
        await apiClient.put(`/api/usuarios/anuncios/${id}`, formData);
        clearFields();
        return loadAnuncios();
    }

    await apiClient.post(`/api/usuarios/${usuarioId}/anuncios`, formData);

    clearFields();
    return loadAnuncios();
}

async function hideOrShow(status, e) {
    e.preventDefault();
    e.stopPropagation();
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const id = document.querySelector('#inputId').value;
    let url = `/api/usuarios/anuncios/${id}`;

    if (!id) {
        return;
    }

    if (status === 'OCULTADO') {
        await apiClient.patch(url + `/exibir`);
        clearFields();
        return loadAnuncios();
    }

    await apiClient.patch(url + '/ocultar');
    clearFields();
    return loadAnuncios();
}

function getFormData() {
    const formData = new FormData();
    const nome = document.querySelector('#inputNome').value;
    const descricao = document.querySelector('#inputDescricao').value;
    const foto = document.querySelector('input[type="file"]').files.length > 0 ? document.querySelector('input[type="file"]').files[0] : '';
    const porte = document.querySelector('#selectPorte').value;
    const sexo = document.querySelector('#selectSexo').value;
    const castrado = document.querySelector('#checkboxCastrado').checked;
    const vacinado = document.querySelector('#checkboxVacinado').checked;
    const contato = document.querySelector('#inputContato').value;
    const tipo = document.querySelector('#selectTipo').value;
    const raca = document.querySelector('#selectRaca').value;

    formData.append('nome', nome);
    formData.append('descricao', descricao);
    if (foto) {
        formData.append('foto', foto);
    }
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
    const inputsAndSelects = form.querySelectorAll('input, select, textarea');
    inputsAndSelects.forEach(input => {
        input.value = '';
        input.checked = false;
    })

    removeAnuncioFocus();
}

function normalizeRaca(raca) {
    raca = raca.replace(/_/g, '-');
    return raca.charAt(0).toUpperCase() + raca.slice(1).toLowerCase();
}