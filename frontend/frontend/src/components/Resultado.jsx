export default function Resultado({ resultado }) {
    if(!resultado) {
        return null;
    }

    const {previsao, probabilidade} = resultado;

    {/*Definir cor da previsão */}
    const corPrevisao = previsao.toLowerCase() === "cancelar" ? "red" 
                    : previsao.toLowerCase() === "continuar" ? "green" 
                    : "black";
    {/*const corPrevisao = previsao === "Churn" ? "red" : "green";*/}

    return (
        <div id="resultado" style={{marginTop: "20px", padding: "10px", border: "1px solid #370ea8ff", borderRadius: "5px"}}>
            <h3>Resultado</h3>
            <p>
                <strong style={{color: corPrevisao}}>{previsao}</strong>
            </p>
            <p><strong>Probabilidade:</strong> {(probabilidade *100).toFixed(2)}%
            </p>
        </div>
    );
}