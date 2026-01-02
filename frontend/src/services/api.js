const API_BASE =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export async function predictChurn(dados) {
   try {
    const response = await fetch(`${API_BASE}/predict`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(dados),
    });

    if (!response.ok) {
      const erro = await response.json();
      console.error('Erro no backend:', erro);
      throw new Error(erro.message || 'Erro na previsão de churn');
    }

    return await response.json();
  } catch (error) {
    console.error('Erro ao conectar com a API:', error);
    throw error;
  }
}


export async function getHistorico() {
  try {
    const response = await fetch(`${API_BASE}/historico`);

    if (!response.ok) {
      throw new Error('Erro ao buscar histórico');
    }

    return await response.json();
  } catch (error) {
    console.error('Erro ao conectar com a API:', error);
    throw error;
  }
}