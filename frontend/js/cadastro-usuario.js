document.addEventListener("DOMContentLoaded", init);

function init() {
    bindBtnCadastro();
}

function bindBtnCadastro() {
    document.querySelector(`#btnCadastro`).addEventListener('click', register);
}

async function register(e){
    e.preventDefault();

    const nome = document.querySelector('#inputNome').value;
    const email = document.querySelector('#inputEmail').value;
    const senha = document.querySelector('#inputPassword').value;

    try {
        const response = await fetch(`/api/usuarios`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nome: nome,
                email: email,
                senha: senha
            })
        });

        if (!response.ok && response.status === 409) {
            const alertContainer = document.getElementById('alertContainer');
            return alertContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Este e-mail já está cadastrado!
                    </div>
                `;
        }

        if (!response.ok) {
            throw new Error('Erro na requisição');
        }

        const alertContainer = document.getElementById('alertContainer');
        alertContainer.innerHTML = `
                    <div class="alert alert-success" role="alert">
                        Usuário criado com sucesso!
                    </div>
                `;
        
        document.querySelector('#inputNome').value = '';
        document.querySelector('#inputEmail').value = '';
        document.querySelector('#inputPassword').value = '';        
    } catch (error) {
        const alertContainer = document.getElementById('alertContainer');
        alertContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Erro ao alterar a senha. Por favor, tente novamente.
                    </div>
                `;
    }
}