export default function TabelaHistorico({listaHistorico}) { 
    if(!listaHistorico.length) {
        return <p>Nenhum histórico disponível.</p>;

        }
    return (
        <table>
            <thead>
                <tr>
                    <th>Data</th>
                    <th>Hora</th>
                    <th>Plano</th>
                    <th>Contrato(Meses)</th>
                    <th>Atrasos</th>
                    <th>Uso</th>
                    <th>Previsão</th>
                    <th>% de Churn</th>
                </tr>
            </thead>
            <tbody>
                {listaHistorico.map((item) => (
                <tr key={item.id}>
                    {/*<td>{new Date(item.dataAnalise).toLocaleString("pt-BR")}</td>*/}
                    <td>{new Date(item.dataAnalise).toLocaleDateString("pt-BR")}</td>
                    <td>{new Date(item.dataAnalise).toLocaleTimeString("pt-BR")}</td>
                    <td>{item.plano.charAt(0).toUpperCase() + item.plano.slice(1)}</td>
                    <td>{item.tempoContratoMeses}</td>
                    <td>{item.atrasosPagamento}</td>
                    <td>{item.usoMensal}</td>
                    <td>{item.previsao}</td>
                    <td>{(item.probabilidade * 100).toFixed(2)}%</td>
                </tr>
        ))}
            </tbody>
        </table>
    );
}