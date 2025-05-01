import React from "react";
import { useDispatch } from "react-redux";
import { addMessage } from "../../redux/message/MessageActions";
import { requestClients } from "../../redux/clients/ClientsActions";
import { clientsApi } from "../../services/api";
import ClientForm from "../../components/addClients/ClientForm";
import "./AddClient.css";

const AddClient = () => {
  const dispatch = useDispatch();

  const handleAddClient = async (formData, resetForm) => {
    try {
      await clientsApi.create({
        ...formData,
        disbursementsAmount: 0,
        servicesAmount: 0,
        amount: 0,
      });
      resetForm();
      dispatch(addMessage("Client was created successfully!"));
      dispatch(requestClients(""));
    } catch (error) {
      if (error.status === 410) {
        dispatch(addMessage(`Please use a different client ID. ${formData.clientId} already exists.`));
      } else {
        dispatch(addMessage("❗ Error occurred while adding data into clients."));
      }
    }
  };

  return <ClientForm onSubmit={handleAddClient} />;
};

export default AddClient;