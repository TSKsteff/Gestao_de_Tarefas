import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import Search from "../../components/todo/Search";
import Filter from "../../components/todo/Filter";
import Todo from "../../components/todo/Todo";
import TodoForm from "../../components/todo/TodoForm";
import "./App.css";
import Modal from "../../components/Modal";

const Home = () => {
  const { signout, tokenValid } = useAuth();
  const {  fetchTasks, deleteTask, updateTask, createTask } = useAuth();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState("");
  const [filter, setFilter] = useState("All");
  const [sort, setSort] = useState("Asc");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [msg, setMsg] = useState(false);
  const token = localStorage.getItem("user_token");
  const userData = JSON.parse(localStorage.getItem("user_data"));
  const userId = userData?.id;

  //useUserActivity(handleUserActivity, 60000);
  // Busca as tarefas ao carregar o componente
  useEffect(() => {
    loadTasks();
  }, []);
  // Carregar as tarefas da API
  const loadTasks = async () => {
    try {
      const todos = await fetchTasks(token.trim());
      setTasks(todos);
      localStorage.setItem("tasks", JSON.stringify(todos));
      if(!todos){
        setMsg(`Token de expirado efetue o login novamente`);
        setIsModalOpen(true);
        setTimeout(() => {
          setIsModalOpen(false);
        }, 3000);
      }
    } catch (error) {
      console.error("Erro ao carregar tarefas:", error);
      setError("Erro ao carregar as tarefas.");
    }
  };

  // Deleta uma tarefa
  const handleDeleteTask = async (taskId) => {
    try {
      await deleteTask(taskId, token);
      const updatedTasks = tasks.filter((task) => task.id !== taskId);
      setTasks(updatedTasks);
      localStorage.setItem("tasks", JSON.stringify(updatedTasks));
    } catch (error) {
      console.error("Erro ao deletar a tarefa:", error);
      setError("Erro ao deletar a tarefa.");
    }
  };

  // Atualiza uma tarefa
  const handleUpdateTask = async (idTask, updatedTask) => {
    /*setMsg(`⚠️ Não foi possível atualizar a tarefa \n"${JSON.stringify(updateTask.titulo)}"\n A função está fora de ar.`);
    setIsModalOpen(true);
    setTimeout(() => {
      setIsModalOpen(false);
    }, 5000);*/
    
    try {
      const updated = await updateTask(idTask, updatedTask, token);
      setTasks((prevTasks) =>
        prevTasks.map((task) => (task.id === idTask ? updated : task))
      );
      localStorage.setItem("tasks", JSON.stringify(tasks));
    } catch (error) {
      console.error("Erro ao atualizar tarefa:", error);
      setError("Erro ao atualizar a tarefa.");
    }
  };
  

  // Adiciona uma nova tarefa
  const handleAddTask = async (newTask) => {
    /*
    setMsg(`⚠️ Não foi possível criar a tarefa \n"${JSON.stringify(newTask.titulo)}"\n\n. A função está fora de ar.`);
    setIsModalOpen(true);
    setTimeout(() => {
      setIsModalOpen(false);
    }, 5000);*/

    try {
      const task = await createTask(newTask, userId, token);
      setTasks([...tasks, task]);
      localStorage.setItem("tasks", JSON.stringify([...tasks, task]));
    } catch (error) {
      console.error("Erro ao criar a tarefa:", error);
      setError("Erro ao criar a tarefa.");
    }
  };

  // Filtra e ordena as tarefas
  const filteredTasks = tasks
  .filter((todo) => todo && todo.titulo) // Filtra tarefas válidas e com título
  .filter((todo) =>
    filter === "All" ? true : todo.status?.toLowerCase() === filter.toLowerCase()
  ) // Filtra por status
  .filter((todo) =>
    todo.titulo?.toLowerCase().includes(search.toLowerCase())
  ) // Filtra por busca no título
  .sort((a, b) => {
    if (sort === "Asc") {
      return new Date(a.dataVencimento) - new Date(b.dataVencimento);
    } else if (sort === "Desc") {
      return new Date(b.dataVencimento) - new Date(a.dataVencimento);
    } else {
      return a.titulo?.localeCompare(b.titulo);
    }
  }); // Ordena as tarefas


  return (
    
    <div className="app">
      <h1 style={{ margin: "10px" }}>Lista de Tarefas</h1>
      {error && <p className="error">{error}</p>}
      <Search search={search} setSearch={setSearch} />
      <Filter filter={filter} handleFilterChange={setFilter} setSort={setSort} />

      <div className="todo-list">
        {filteredTasks.length > 0 ? (
          filteredTasks.map((todo) => (
            <Todo
              key={todo.id}
              todo={todo}
              updateStatus={handleUpdateTask} // Agora a função é passada corretamente
              removeTodo={() => handleDeleteTask(todo.id)}
            />
          ))
        ) : (
          <p>Nenhuma tarefa encontrada.</p>
        )}
      </div>

      <TodoForm addTodo={handleAddTask} />
      {isModalOpen && <Modal isOpen={isModalOpen} message={msg} />}

      {tokenValid && (
        <button
          onClick={() => {
            signout();
            navigate("/");
          }}
          className="logout-button"
        >
          Sair
        </button>
      )}
    </div>
  );
};

export default Home;
