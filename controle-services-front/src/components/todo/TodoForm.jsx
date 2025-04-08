import { useState } from "react";
import DateInput from "./date";

const TodoForm = ({ addTodo }) => {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [dataVencimento, setDataVencimento] = useState("");
  const [status, setStatus] = useState("");
  const [error, setError] = useState({ titulo: "", descricao: "", dataVencimento: "", status: "" });

  const validateField = (field, value) => {
    setError((prev) => ({
      ...prev,
      [field]: value ? "" : "Campo obrigatório",
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!titulo || !descricao || !dataVencimento || !status) {
      setError({
        titulo: titulo ? "" : "Campo obrigatório",
        descricao: descricao ? "" : "Campo obrigatório",
        dataVencimento: dataVencimento ? "" : "Campo obrigatório",
        status: status ? "" : "Campo obrigatório",
      });
      return;
    }

    const newTask = { titulo, descricao, dataVencimento, status };
    console.log('new taks',JSON.stringify(newTask));
    addTodo(newTask);

    setTitulo("");
    setDescricao("");
    setDataVencimento("");
    setStatus("");
    setError({ titulo: "", descricao: "", dataVencimento: "", status: "" });
  };

  return (
    <div className="todo-form">
      <h2>Criar tarefa:</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <input
            type="text"
            className="input"
            value={titulo}
            placeholder="Digite o título"
            onChange={(e) => {
              setTitulo(e.target.value);
              validateField("titulo", e.target.value);
            }}
          />
          {error.titulo && <p className="error">{error.titulo}</p>}
        </div>

        <div>
          <input
            type="text"
            className="input"
            value={descricao}
            placeholder="Digite a descrição"
            onChange={(e) => {
              setDescricao(e.target.value);
              validateField("descricao", e.target.value);
            }}
          />
          {error.descricao && <p className="error">{error.descricao}</p>}
        </div>

        <div>
          <DateInput
            value={dataVencimento}
            onChange={(date) => {
              setDataVencimento(date);
              validateField("dataVencimento", date);
            }}
          />
          {error.dataVencimento && <p className="error">{error.dataVencimento}</p>}
        </div>

        <div>
          <select
            value={status}
            onChange={(e) => {
              setStatus(e.target.value);
              validateField("status", e.target.value);
            }}
            style={{ marginTop: "10px" }}
          >
            <option value="">Selecione um status</option>
            <option value="EM_ANDAMENTO">EM_ANDAMENTO</option>
            <option value="PENDENTE">PENDENTE</option>
            <option value="CONCLUIDO">CONCLUIDO</option>
          </select>
          {error.status && <p className="error">{error.status}</p>}
        </div>

        <button type="submit">Criar tarefa</button>
      </form>
    </div>
  );
};

export default TodoForm;
