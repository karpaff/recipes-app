import React from "react";
import Sidebar from "./Sidebar";
import "./PageLayout.css"

interface PageLayoutProps {
  children: React.ReactNode;
}

const PageLayout: React.FC<PageLayoutProps> = ({ children }) => {
  return (
    <div className="page-layout">
      <Sidebar />
      <main>
        {children}
      </main>
    </div>
  );
};

export default PageLayout;
