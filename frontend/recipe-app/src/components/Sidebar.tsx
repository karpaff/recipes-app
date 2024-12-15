import React from "react";
import { Link } from "react-router-dom";
import "./Sidebar.css"

const Sidebar: React.FC = () => {
  return (
    <aside className="sidebar">
      <nav>
        <ul>
          <li><Link to="/">Recipes</Link></li>
          <li><Link to="/favourites">Favourites</Link></li>
          <li><Link to="/profile">Profile</Link></li>
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;
