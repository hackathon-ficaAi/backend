export default function Resultado({ resultado }) {
  if (!resultado) return null;

// Define a classe de acordo com a previsão
  const classe = resultado.previsao.toLowerCase().includes("cancelar")
  ? "error"   // vermelho
  : "success"; // verde

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
