document.addEventListener("DOMContentLoaded", init);

function init() {
    bindBtnEntrar();
}

function bindBtnEntrar() {
    document.querySelector(`#btnEntrar`).addEventListener('click', doLogin);
}

async function doLogin(e) {
    e.preventDefault();
    const login = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;

    const data = {
        email: login,
        senha: password
    };

    try {
        const response = await fetch(`/api/admin/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const alertContainer = document.getElementById('alertContainer');
            alertContainer.innerHTML = `
                    <div class="alert alert-danger" role="alert">
                        Erro ao realizar login. Por favor, verifique suas credenciais e tente novamente.
                    </div>
                `;
        }

        const result = await response.json();
        localStorage.setItem('adminToken', result.token);
        window.location.href = '/admin/ativos.html';
    } catch (error) {   
        const alertContainer = document.getElementById('alertContainer');
        return alertContainer.innerHTML = `
        <div class="alert alert-danger" role="alert">
            Erro ao realizar login. Por favor, verifique suas credenciais e tente novamente.
        </div>
    `;
    }
}