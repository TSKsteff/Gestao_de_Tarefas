import { createContext, useEffect, useState } from "react";

export const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [tokenValid, setTokenValid] = useState(true);

  useEffect(() => {
    try {
      const userToken = localStorage.getItem("user_token");
      const userData = localStorage.getItem("user_data");

      if (userToken && userData) {
        const parsedUser = JSON.parse(userData);
        setUser(parsedUser);
      }
    } catch (error) {
      console.error("Erro ao recuperar dados do localStorage:", error);
      localStorage.removeItem("user_token");
      localStorage.removeItem("user_data");
    }
  }, []);

  const signin = async (email, password) => {
    try {
      const response = await fetch("http://localhost:8000/api/users/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });
  
      const data = await response.json();
  
      if (!response.ok) {
        return "E-mail ou senha incorretos";
      }
  
      const { token, id } = data;
      if (!token || !id) {
        return "Erro: Token ou ID não recebido do servidor";
      }
  
      localStorage.setItem("user_token", token);
      localStorage.setItem("user_data", JSON.stringify({ email, id }));
      localStorage.setItem("dados", JSON.stringify({ email, password }));
      setUser({ email, id });
      setTokenValid(true);
    } catch (error) {
      console.error("Erro na requisição (signin):", error);
      return "Erro ao conectar com o servidor";
    }
  };

  const signup = async (name, email, password) => {
    try {
      const response = await fetch("http://localhost:8000/api/users/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ name, email, password }),
      });
  
      const data = await response.json();
  
      if (!response.ok) {
        return "Email já cadastrado. Tente outro.";
      }
  
      if (!data.id) {
        return "Erro inesperado: Usuário não foi criado corretamente";
      }
    } catch (error) {
      console.error("Erro na requisição (signup):", error);
      return "Erro ao conectar com o servidor";
    }
  };

  const getUserById = async (id, token) => {
    try {
      const response = await fetch(`http://localhost:8000/api/auth/${id}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      return await response.json();
    } catch (error) {
      console.error("Erro ao buscar Usuario:", error);
      return null;
    }
  };

  const getMe = async (token) => {
    try {
      const response = await fetch(`http://localhost:8000/api/auth/me`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      return await response.json();
    } catch (error) {
      console.error("Erro ao autenticar:", error);
      return null;
    }
  };

  const fetchTasks = async (token) => {
    try {
      const me = await getMe(token); // Aguarda resposta antes de verificar
  
      if (!me || me.error) { // Se o token for inválido ou expirado
        return null;
      }
  
      const response = await fetch("http://localhost:8000/api/auth/task", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
  
      if (!response.ok) {
        throw new Error("Erro ao recuperar tasks");
      }
  
      return await response.json();
    } catch (error) {
      console.error("Erro na requisição (fetchTasks):", error);
      return null;
    }
  };
  
  
  const updateTask = async (id, task, token) => {
    try {
      await fetch(`http://localhost:8000/api/auth/task/${id}/update-task`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(task),
      });
  
      return await fetchTasks(token);
    } catch (error) {
      console.error("Erro ao atualizar tarefa:", error);
      return null;
    }
  };
  
  const deleteTask = async (id, token) => {
    try {
      await fetch(`http://localhost:8000/api/auth/task/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
  
      await fetchTasks(token);
    } catch (error) {
      console.error("Erro ao excluir tarefa:", error);
    }
  };
  
  const getTaskById = async (id, token) => {
    try {
      const response = await fetch(`http://localhost:8000/api/auth/task/${id}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      return await response.json();
    } catch (error) {
      console.error("Erro ao buscar tarefa:", error);
      return null;
    }
  };
  
  const createTask = async (task, userId, token) => {
    try {
      await fetch(`http://localhost:8000/api/auth/task/${userId}/create-task`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(task),
      });
  
      await fetchTasks(token);
    } catch (error) {
      console.error("Erro ao criar tarefa:", error);
    }
  };
  
  const signout = () => {
    try {
      setUser(null);
      setTokenValid(false);
      localStorage.removeItem("user_token");
      localStorage.removeItem("user_data");
    } catch (error) {
      console.error("Erro ao limpar localStorage:", error);
    }
  };

  return (
    <AuthContext.Provider value={{ user, signed: !!user, tokenValid, signin, signout, signup, fetchTasks, updateTask, deleteTask, getTaskById, createTask, getUserById }}>
      {children}
    </AuthContext.Provider>
  );
};
