import APIClient from '../js/APIClient.js';

document.addEventListener("DOMContentLoaded", init);

function init() {
    loadAnuncio();
}

async function loadAnuncio(){
    const token = localStorage.getItem('userToken');
    const apiClient = new APIClient(token);
    const anuncioId = getIdFromUrl();
    const data = await apiClient.get(`/api/common/anuncios/${anuncioId}`);
    fillAnuncioInfo(data);
}

function getIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get('id');
}

function fillAnuncioInfo(data){
    const lookingForHome = document.querySelector(".looking-for-home");
    lookingForHome.innerText = `Oi, eu sou o ${data.nome}`
    const containerAnuncio = document.querySelector(".container-anuncio");
    const sexoAndVacinado = getSexoAndVacinado(data);
    const raca = normalizeRaca(data.raca);
    containerAnuncio.id = data.id;
    containerAnuncio.innerHTML = `
    <div class="d-flex">
                    <img class="foto"
                        src="data:image/jpeg;base64,${data.foto}"
                        alt="">
                </div>
                <div class="container-fluid d-flex flex-column info-anuncio">
                    <div class="d-flex flex-column justify-content-between info-anuncio-main">
                        <div class="d-flex justify-content-between align-items-center">
                            <p>Raça: ${raca}</p>
                            <div class="anuncio-icons">
                                ${sexoAndVacinado}
                            </div>
                        </div>
                        <p>Porte: ${data.porte}</p>
                        <p><i class="fa-solid fa-phone"></i>${formatPhone(data.contato)}</p>
                        <p>${data.castrado ? "Castrado" : "Não-castrado"}</p>
                    </div>
                    <p class="info-anuncio-description">
                        ${data.descricao}
                    </p>
                </div>
    `

}

function normalizeRaca(raca){
    raca = raca.replace(/_/g, '-');
    return raca.charAt(0).toUpperCase() + raca.slice(1).toLowerCase();
}

function getSexoAndVacinado(data){
    let sexo = '';
    let vacinado = '';
    if(data.sexo === 'M'){
        sexo = `<i title="Macho" class="fa-solid fa-mars"></i>`;
    } else {
        sexo = `<i title="Fêmea" class="fa-solid fa-venus"></i>`;
    }

    if(data.vacinado){
        vacinado = `<i title="Vacinado" class="fa-solid fa-syringe"></i>`;
    } else {
        vacinado = `<i title="Não-vacinado" style="color:#b1b1b1" class="fa-solid fa-syringe"></i>`;
    }

    return sexo + vacinado;
}

function formatPhone(phone) {
    const digitsOnly = phone.replace(/\D/g, '');

    if (digitsOnly.length === 10 || digitsOnly.length === 11) {
        if (digitsOnly.length === 10) {
            return `(${digitsOnly.slice(0, 2)}) ${digitsOnly.slice(2, 7)}-${digitsOnly.slice(7)}`;
        } else {
            return `(${digitsOnly.slice(0, 2)}) ${digitsOnly.slice(2, 7)}-${digitsOnly.slice(7)}`;
        }
    } else {
        return phone;
    }
}
