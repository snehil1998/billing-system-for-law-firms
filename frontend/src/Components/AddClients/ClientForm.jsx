import React, { useState } from "react";
import FormField from "../common/FormField";
import ButtonGroup from "../common/ButtonGroup";

const initialFormState = {
  clientId: "",
  clientName: "",
  currencyCode: "",
};

const ClientForm = ({ onSubmit }) => {
  const [form, setForm] = useState(initialFormState);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.clientId || !form.clientName || !form.currencyCode) return;
    onSubmit(form, () => setForm(initialFormState));
  };

  const handleReset = () => setForm(initialFormState);

  return (
    <form onSubmit={handleSubmit} onReset={handleReset} className="add-client-form">
      <FormField id="client-id" name="clientId" label="Client ID" value={form.clientId} onChange={handleChange} required />
      <FormField id="client-name" name="clientName" label="Client Name" value={form.clientName} onChange={handleChange} required />
      <FormField id="currency-code" name="currencyCode" label="Currency Code" value={form.currencyCode} onChange={handleChange} required />
      <ButtonGroup submitLabel="Add Client" resetLabel="Clear" />
    </form>
  );
};

export default ClientForm; 