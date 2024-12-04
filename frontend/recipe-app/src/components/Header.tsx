import React from "react";

const Header: React.FC = () => {
  return (
    <header className="header">
      <input type="text" placeholder="Search something..." />
      <button>Search</button>
    </header>
  );
};

export default Header;
