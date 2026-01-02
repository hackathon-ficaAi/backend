export default function Resultado({ resultado }) {
  if (!resultado) return null;

  if (!resultado.previsao || typeof resultado.probabilidade !== "number") {
    return (
      <div className="error">
        <h3>Resultado</h3>
        <p>Não foi possível gerar a previsão.</p>
      </div>
    );
  }

  const classe = resultado.previsao.toLowerCase().includes("alto")
    ? "error"
    : "success";

  return (
    <div id="resultado" className={classe}>
      <h3>Resultado</h3>
      <p>
        <strong>Previsão:</strong> {resultado.previsao}
      </p>
      <p>
        <strong>Probabilidade:</strong>{" "}
        {(resultado.probabilidade * 100).toFixed(2)}%
      </p>
    </div>
  );
}
