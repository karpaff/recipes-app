import React from "react";
import Sidebar from "./Sidebar";
import Header from "./Header";

interface PageLayoutProps {
  children: React.ReactNode;
}

const PageLayout: React.FC<PageLayoutProps> = ({ children }) => {
  return (
    <div className="page-layout">
      <Sidebar />
      <main>
        <Header />
        {children}
      </main>
    </div>
  );
};

export default PageLayout;
