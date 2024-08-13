document.addEventListener("DOMContentLoaded", init);

function init() {
    bindBtnEnviarEmail();
}

function bindBtnEnviarEmail() {
    document.querySelector(`#btnEnviarEmail`).addEventListener('click', sendMail);
}

async function sendMail(e) {
    e.preventDefault();

    const url = '/api/usuarios/recuperar-senha';
    const email = document.getElementById('email').value;
    const urlWithParams = `${url}?email=${encodeURIComponent(email)}`;

    try {
        const response = await fetch(urlWithParams, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (!response.ok) {
            throw new Error('Erro na requisição');
        }

        const alertContainer = document.getElementById('alertContainer');
        alertContainer.innerHTML = `
                    <div class="alert alert-success" role="alert">
                        Email enviado com sucesso!
                    </div>
                `;
    } catch (error) {
        console.error('Erro:', error);

        const alertContainer = document.getElementById('alertContainer');
        alertContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Erro ao enviar o e-mail de recuperação. Por favor, tente novamente.
                    </div>
                `;
    }

}