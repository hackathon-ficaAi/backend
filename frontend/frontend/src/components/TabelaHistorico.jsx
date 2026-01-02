export default function TabelaHistorico({ listaHistorico }) {
  if (!listaHistorico.length) {
    return <p>Nenhum histórico disponível.</p>;
  }
  return (
    <table>
      <thead>
        <tr>
          <th>Data</th>
          <th>Hora</th>
          <th>País</th>
          <th>Idade</th>
          <th>Produtos</th>
          <th>Contrato (meses)</th>
          <th>Previsão</th>
          <th>Probabilidade</th>
        </tr>
      </thead>
      <tbody>
        {listaHistorico.map((item) => {
          const data = new Date(item.dataAnalise);
          const dataFormatada = data.toLocaleDateString("pt-BR");
          const horaFormatada = data.toLocaleTimeString("pt-BR");

          return (
            <tr key={item.id}>
              <td>{dataFormatada}</td>
              <td>{horaFormatada}</td>
              <td>{item.pais}</td>
              <td>{item.idade}</td>
              <td>{item.numProdutos}</td>
              <td>{item.tempoContratoMeses}</td>
              <td>{item.previsao ?? "—"}</td>
              <td>
                {item.probabilidade !== null
                  ? `${(item.probabilidade * 100).toFixed(2)}%`
                  : "—"}
              </td>
            </tr>
          );
        })}
      </tbody>
    </table>
  );
}
