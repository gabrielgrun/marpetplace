export async function fetchGetRequest(url, token) {    
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            if(response.status == 403){
                //TODO: Verificar o que fazer com permissao (vou criar uma pagina?)
                //Token expirado também cai aqui
                throw new Error('a');
            }
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Houve um problema com a requisição:', error);
    }
}

export async function fetchPatchRequest(url, token) {    
    try {
        const response = await fetch(url, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            if(response.status == 403){
                //TODO: Verificar o que fazer com permissao (vou criar uma pagina?)
                //Token expirado também cai aqui
                throw new Error('a');
            }
            throw new Error('Network response was not ok ' + response.statusText);
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Houve um problema com a requisição:', error);
    }
}

export async function fetchDeleteRequest(url, token) {
    try {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const result = await response.json();
        return result;
    } catch (error) {
        console.error('Houve um problema com a requisição:', error);
    }
}