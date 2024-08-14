document.addEventListener("DOMContentLoaded", init);

function init() {
    bindBtnAlterarSenha();
}

function bindBtnAlterarSenha() {
    document.querySelector(`#btnAlterarSenha`).addEventListener('click', changePassword);
}

async function changePassword(e){
    e.preventDefault();

    const baseUrl = '/api/usuarios/alterar-senha';
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const newPassword = document.getElementById('newPassword').value;
    const urlWithParams = `${baseUrl}?token=${encodeURIComponent(token)}&password=${encodeURIComponent(newPassword)}`;

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
                        Senha alterada com sucesso!
                    </div>
                `;
    } catch (error) {
        console.error('Erro:', error);

        const alertContainer = document.getElementById('alertContainer');
        alertContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Erro ao alterar a senha. Por favor, tente novamente.
                    </div>
                `;
    }
}