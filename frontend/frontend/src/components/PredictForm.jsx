import { useState } from "react";
import { predictChurn } from "../services/api";
import Resultado from "./Resultado";

export default function PredictForm(onVerHistorico) {
  const [formData, setFormData] = useState({
    tempoContratoMeses: "",
    atrasosPagamento: "",
    usoMensal: "",
    suporteChamados: "",
  });

  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErro(null);
    setResultado(null);

    try {
      const response = await predictChurn({
        tempo_contrato_meses: Number(formData.tempoContratoMeses),
        atrasos_pagamento: Number(formData.atrasosPagamento),
        uso_mensal: Number(formData.usoMensal),
        plano: formData.plano,
      });

      setResultado(response);
    } catch (err) {
      setErro("Erro ao conectar com o backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      {/* Logo e título */}
      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "12px",
          marginBottom: "20px",
        }}
      >
        <img
          src="public/FicaAI_logo.png"
          alt="FicaAI_Logo"
          style={{ height: "50px" }}
        />
        <h1 style={{ margin: 0 }}>Previsão de Churn</h1>
      </div>

      <form onSubmit={handleSubmit}>
        <label>Tempo de Contrato (meses)</label>
        <input
          type="number"
          name="tempoContratoMeses"
          value={formData.tempoContratoMeses}
          onChange={handleChange}
          required
        />

        <label>Atrasos no Pagamento</label>
        <input
          type="number"
          name="atrasosPagamento"
          value={formData.atrasosPagamento}
          onChange={handleChange}
          required
        />

        <label>Uso Mensal (GB)</label>
        <input
          type="number"
          name="usoMensal"
          value={formData.usoMensal}
          onChange={handleChange}
          required
        />

        <label>Plano</label>
        <select
          name="plano"
          value={formData.plano}
          onChange={handleChange}
          required
        >
          <option value="">-- Escolha --</option>
          <option value="basico">Básico</option>
          <option value="premium">Premium</option>
        </select>

        {/*<label>Chamados ao Suporte</label>
        <input
          type="number"
          name="suporteChamados"
          value={formData.suporteChamados}
          onChange={handleChange}
          required
        />*/}

        <div
          className="actions"
          style={{ marginTop: "16px", display: "flex", gap: "10px" }}
        >
          {/* Botão de submissão do formulário */}
          <button type="submit" disabled={loading}>
            {loading ? "Processando..." : "Prever Churn"}
          </button>

          {/* Botão de limpar o formulário */}
          <button
            type="button"
            onClick={() =>
              setFormData({
                tempoContratoMeses: "",
                atrasosPagamento: "",
                usoMensal: "",
                plano: "",
              })
            }
          >
            Limpar
          </button>

          {/* Botão de ver histórico 
          {onVerHistorico && (
            <button
              type="button"
              className="secondary"
              onClick={onVerHistorico}
            >
              Ver Histórico
            </button>
          )}*/}
        </div>
      </form>

      {erro && <div className="error">{erro}</div>}
      <Resultado resultado={resultado} />
    </div>
  );
}
