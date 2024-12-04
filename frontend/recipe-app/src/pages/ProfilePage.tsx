import React, { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

const ProfilePage: React.FC = () => {
  const authContext = useContext(AuthContext);

  if (!authContext) return null;

  const { logout } = authContext;

  return (
    <div className="profile-page">
      <h1>Profile</h1>
      <button onClick={logout}>Logout</button>
    </div>
  );
};

export default ProfilePage;
