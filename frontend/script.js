const form = document.getElementById("form-churn");
const resultadoDiv = document.getElementById("resultado");
const API_URL = "http://localhost:8080/api/predict";


form.addEventListener("submit", async (event) => {
    event.preventDefault(); // evita reload da página

    //Monta o JSON exatamente como o backend espera
    const dados = {
        tempo_contrato_meses: Number(document.getElementById("tempoContrato").value),
        atrasos_pagamento: Number(document.getElementById("atrasosPagamento").value),
        uso_mensal: Number(document.getElementById("usoMensal").value),
        plano: document.getElementById("plano").value
    };

    resultadoDiv.innerHTML = "Carregando...";

    try {
        //Chamada ao backend
        const response = await fetch(API_URL, {
        method: "POST",
        headers: {
        "Content-Type": "application/json"
    },
    body: JSON.stringify(dados)
});


        //Se der erro HTTP (400, 500, etc)
        if (!response.ok) {
            const erro = await response.json();
            mostrarErro(erro);
            return;
        }

        //Sucesso
        const resultado = await response.json();
        mostrarResultado(resultado);

    } catch (error) {
        resultadoDiv.innerHTML = "Erro ao conectar com o backend.";
        console.error(error);
    }
});

function mostrarResultado(resultado) {
    resultadoDiv.innerHTML = `
        <h3>Resultado</h3>
        <p><strong>Previsão:</strong> ${resultado.previsao}</p>
        <p><strong>Probabilidade:</strong> ${(resultado.probabilidade * 100).toFixed(2)}%</p>
    `;
}

function mostrarErro(erro) {
    let html = `<h3>Erro</h3><ul>`;

    if (erro.detalhes) {
        for (const campo in erro.detalhes) {
            html += `<li>${campo}: ${erro.detalhes[campo]}</li>`;
        }
    } else {
        html += `<li>${erro.mensagem || "Erro desconhecido"}</li>`;
    }

    html += `</ul>`;
    resultadoDiv.innerHTML = html;
}
