import styled from "styled-components";

export const Container = styled.div`
 display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 10px;
  height: 100vh;
  padding: 20px;
`;

export const Content = styled.div`
   gap: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  width: 100%;
  max-width: 350px;
  box-shadow: 0 1px 2px #0003;
  background-color: white;
  padding: 20px;
  border-radius: 5px;

  @media (max-width: 480px) {
    max-width: 90%; /* Reduz o tamanho em telas pequenas */
    padding: 15px;
  }
`;

export const Label = styled.label`
  font-size: 18px;
  font-weight: 600;
  color: #676767;
  text-align: center;
`;

export const LabelSignup = styled.label`
  font-size: 16px;
  color: #676767;
  text-align: center;
`;

export const labelError = styled.label`
  font-size: 14px;
  color: red;
  text-align: center;
`;

export const Strong = styled.strong`
  cursor: pointer;

  a {
    text-decoration: none;
    color: #676767;
  }
`;
