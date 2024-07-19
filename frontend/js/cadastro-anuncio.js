import Jwt from '../js/Jwt.js';
import APIClient from '../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);


function init() {
    maskInput();
    bindBtnSave();
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