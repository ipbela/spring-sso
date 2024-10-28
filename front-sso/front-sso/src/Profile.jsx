import React, { useEffect, useState } from "react";
import axios from "axios";

const Profile = () => {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await axios.get('http://localhost:5173/userinfo');
        const userData = response.data;

        setUserInfo(userData);
        localStorage.setItem("id_token", userData.id_token); // Armazena o Token no LocalStorage
        
      } catch (error) {
        console.error("Erro ao buscar informações do usuário:", error);
        setError("Erro ao buscar informações do usuário.");
      }
    };

    fetchUserInfo();
  }, []);

  // Apenas renderiza as informações
  return (
    <div>
      {error && <p>{error}</p>}
      {userInfo ? (
        <ul>
          {userInfo.user_attributes && 
            Object.entries(userInfo.user_attributes).map(([key, value]) => (
              <li key={key}>
                <span style={{ fontWeight: "bold" }}>{key}</span>: <span>{value}</span>
              </li>
            ))}
        </ul>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Profile;
