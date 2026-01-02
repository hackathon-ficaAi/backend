import { useState } from "react";
import { predictChurn } from "../services/api";
import Resultado from "./Resultado";

export default function PredictForm() {
  const [formData, setFormData] = useState({
    creditScore: "",
    tempoContrato: "",
    temCartaoCredito: false,
    pais: "",
    genero: "",
    idade: "",
    numProdutos: "",
    membroAtivo: false,
    saldo: "",
    salarioEstimado: "",
  });

  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState(null);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type == "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErro(null);
    setResultado(null);

    try {
      const payload = {
        credit_score: Number(formData.creditScore),
        tenure: Number(formData.tempoContrato),
        tem_cartao_credito: formData.temCartaoCredito,
        pais: formData.pais,
        genero: formData.genero,
        idade: Number(formData.idade),
        num_produtos: Number(formData.numProdutos),
        membro_ativo: formData.membroAtivo,
        saldo: Number(formData.saldo),
        salario_estimado: Number(formData.salarioEstimado),
      };
      const response = await predictChurn(payload);

      const resultadoAdaptado = {
        previsao: response.previsao_churn,
        probabilidade: response.probabilidade_churn,
      };

      setResultado(resultadoAdaptado);
    } catch (err) {
      setErro(err.message || "Erro ao conectar com o backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "12px",
          marginBottom: "20px",
        }}
      >
        <img
          src="./FicaAI_logo.png"
          alt="FicaAI_Logo"
          style={{ height: "50px" }}
        />
        <h1 style={{ margin: 0 }}>Previsão de Churn</h1>
      </div>

      <form onSubmit={handleSubmit}>
        <input
          name="creditScore"
          type="number"
          placeholder="Score de Crédito"
          value={formData.creditScore}
          onChange={handleChange}
          required
        />

        <input
          name="tempoContrato"
          type="number"
          placeholder="Tempo de contrato (meses)"
          value={formData.tempoContrato}
          min={"0"}
          onChange={handleChange}
          required
        />

        <input
          name="pais"
          placeholder="País"
          value={formData.pais}
          onChange={handleChange}
          required
        />

        <label>Gênero</label>
        <select
          name="genero"
          value={formData.genero}
          onChange={handleChange}
          required
        >
          <option value="">Selecione</option>
          <option value="Masculino">Masculino</option>
          <option value="Feminino">Feminino</option>
        </select>

        <input
          name="idade"
          type="number"
          placeholder="Idade"
          value={formData.idade}
          min={"0"}
          onChange={handleChange}
          required
        />

        <input
          name="numProdutos"
          type="number"
          placeholder="Número de Produtos"
          value={formData.numProdutos}
          min={"0"}
          onChange={handleChange}
          required
        />

        <input
          name="saldo"
          type="number"
          placeholder="Saldo"
          value={formData.saldo}
          onChange={handleChange}
          required
        />

        <input
          name="salarioEstimado"
          type="number"
          placeholder="Salário Estimado"
          value={formData.salarioEstimado}
          min={"0"}
          onChange={handleChange}
          required
        />

        <label className="checkbox-item">
          <input
            type="checkbox"
            name="temCartaoCredito"
            onChange={handleChange}
          />
          Possui cartão de crédito
        </label>

        <label className="checkbox-item">
          <input type="checkbox" name="membroAtivo" onChange={handleChange} />
          Membro ativo
        </label>

        <button type="submit" disabled={loading}>
          {loading ? "Processando..." : "Prever"}
        </button>
      </form>

      {erro && <div className="error">{erro}</div>}
      <Resultado resultado={resultado} />
    </div>
  );
}
