import React, { useState } from "react";

const DateInput = ({ value, onChange }) => {
  const [error, setError] = useState("");

  const handleDateChange = (e) => {
    const selectedDate = e.target.value;
    const currentDate = new Date().toISOString().split("T")[0];

    if (selectedDate <= currentDate) {
      setError("A data de vencimento deve ser maior que a data atual.");
      onChange(""); // Envia string vazia para limpar o campo no TodoForm
    } else {
      setError("");
      onChange(selectedDate); // Atualiza a data no TodoForm corretamente
    }
  };

  return (
    <div>
      <input
        type="date"
        className="input"
        value={value}
        onChange={handleDateChange}
        min={new Date().toISOString().split("T")[0]}
      />
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default DateInput;
