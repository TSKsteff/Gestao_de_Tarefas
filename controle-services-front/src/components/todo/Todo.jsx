import React from "react";

const statusOptions = ["Pendente", "Em andamento", "Concluído"];

const Todo = ({ todo, updateStatus, removeTodo }) => {
  console.log("Tasks:", todo);

  // Reorganiza as opções para garantir que o status atual fique primeiro
  const orderedStatusOptions = [
    todo.status,
    ...statusOptions.filter((status) => status !== todo.status),
  ];

  // Função para lidar com a mudança de status
  const handleChangeStatus = (e) => {
    const newStatus = e.target.value;
    const updatedTask = {
      ...todo,
      status: newStatus,
    };
    updateStatus(todo.id, updatedTask); // Agora está correto!
  };

  return (
    <div className="todo">
      <div className="content">
        <h4>{todo.titulo} - {todo.dataVencimento}</h4>
        <p>{todo.descricao}</p>
      </div>
      <div className="statusDelete">
        <select
          className="complete"
          value={todo.status}
          onChange={handleChangeStatus} // Chama a função ao alterar o status
        >
          {orderedStatusOptions.map((status) => (
            <option key={status} value={status}>
              {status}
            </option>
          ))}
        </select>
        
        <button className="remove" onClick={() => removeTodo(todo.id)}>
          x
        </button>
      </div>
    </div>
  );
};

export default Todo;
