import React, { useState } from "react";
import Input from "../../components/Input";
import Button from "../../components/Button";
import * as C from "./styles";
import { Link, useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";
import Modal from "../../components/Modal";

const Signin = () => {
  const { signin } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [error, setError] = useState("");
  const [showModal, setShowModal] = useState(false); 

  const handleLogin = async () => {
    if (!email || !senha) {
      setError("Preencha todos os campos");
      return;
    }

    const res = await signin(email, senha);
    
    if (res) {
      setError(res);
      return;
    }

    
    setShowModal(true); // Exibe o modal
    setTimeout(() => {
      setShowModal(false);
      navigate("/home"); // Redireciona após o modal fechar
    }, 1000);
  };

  return (
    <C.Container>
      <C.Label>Sistema de Gestão de Tarefas</C.Label>
      <C.Content>
        <C.Label>Login</C.Label>
        <Input
          type="email"
          placeholder="Digite seu E-mail"
          value={email}
          onChange={(e) => [setEmail(e.target.value), setError("")]}
        />
        <Input
          type="password"
          placeholder="Digite sua Senha"
          value={senha}
          onChange={(e) => [setSenha(e.target.value), setError("")]}
        />
        <C.labelError>{error}</C.labelError>
        <Button Text="Entrar" onClick={handleLogin} />
        <C.LabelSignup>
          Não tem uma conta?
          <C.Strong>
            <Link to="/signup">&nbsp;Registre-se</Link>
          </C.Strong>
        </C.LabelSignup>
      </C.Content>
        {showModal && <Modal isOpen={showModal} message="🚀 Login efetuado com sucesso!" />}
    </C.Container>
  );
};

export default Signin;
