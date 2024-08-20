class APIClient {
  constructor(token) {
    this.token = token;
  }

  async get(url) {
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.token}`
        }
      });

      if (!response.ok) {
        if (response.status === 403) {
          window.location.href = '/login.html';
        }
        throw new Error('Network response was not ok ' + response.statusText);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      throw error;
    }
  }

  async post(url, body) {
    const isFormData = body instanceof FormData;
    const headers = {
      'Authorization': `Bearer ${this.token}`
    };
    if (!isFormData) {
      headers['Content-Type'] = 'application/json';
      body = JSON.stringify(body);
    }

    try {
      const response = await fetch(url, {
        method: 'POST',
        headers: headers,
        body: body
      });

      if (!response.ok) {
        if (response.status === 403) {
          throw new Error('Forbidden: Permissão negada ou token expirado.');
        }
        throw new Error('Network response was not ok ' + response.statusText);
      }

      if(response.json.length){
        const data = await response.json();
        return data;
      }

      return;
    } catch (error) {
      console.error('Houve um problema com a requisição:', error);
      throw error;
    }
  }

  async put(url, body) {
    const isFormData = body instanceof FormData;
    const headers = {
      'Authorization': `Bearer ${this.token}`
    };
    if (!isFormData) {
      headers['Content-Type'] = 'application/json';
      body = JSON.stringify(body);
    }

    try {
      const response = await fetch(url, {
        method: 'PUT',
        headers: headers,
        body: body
      });

      if (!response.ok) {
        if (response.status === 403) {
          throw new Error('Forbidden: Permissão negada ou token expirado.');
        }
        throw new Error('Network response was not ok ' + response.statusText);
      }

      if(response.json.length){
        const data = await response.json();
        return data;
      }

      return;
    } catch (error) {
      console.error('Houve um problema com a requisição:', error);
      throw error;
    }
  }

  async patch(url) {
    try {
      const response = await fetch(url, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.token}`
        }
      });

      if (!response.ok) {
        if (response.status === 403) {
          // TODO: Verificar o que fazer com permissão (vou criar uma pagina?)
          // Token expirado também cai aqui
          throw new Error('a');
        }
        throw new Error('Network response was not ok ' + response.statusText);
      }

      if(response.json.length){
        const data = await response.json();
        return data;
      }

      return;
    } catch (error) {
      console.error('Houve um problema com a requisição:', error);
      throw error; // Re-lança o erro para ser tratado pelo chamador
    }
  }

  async delete(url) {
    try {
      const response = await fetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${this.token}`
        }
      });

      if (!response.ok) {
        if (response.status === 403) {
          // TODO: Verificar o que fazer com permissão (vou criar uma pagina?)
          // Token expirado também cai aqui
          throw new Error('a');
        }
        throw new Error('Network response was not ok ' + response.statusText);
      }

      if(response.json.length){
        const data = await response.json();
        return data;
      }

      return;
    } catch (error) {
      console.error('Houve um problema com a requisição:', error);
      throw error; // Re-lança o erro para ser tratado pelo chamador
    }
  }
}

export default APIClient;