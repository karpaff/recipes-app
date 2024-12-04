// src/App.tsx
import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { RecipeProvider } from "./context/RecipeContext";
import PageLayout from "./components/PageLayout";
import RecipesPage from "./pages/RecipesPage";
import FavouritesPage from "./pages/FavouritesPage";
import ProfilePage from "./pages/ProfilePage";
import RegisterPage from "./pages/RegisterPage";
import LoginPage from "./pages/LoginPage";
import RecipePage from "./pages/RecipePage";

const App: React.FC = () => {
  return (
    <AuthProvider>
      <RecipeProvider>
        <Router>
          <PageLayout>
            <Routes>
              <Route path="/" element={<RecipesPage />} />
              <Route path="/recipes/:id" element={<RecipePage />} />
              <Route path="/favourites" element={<FavouritesPage />} />
              <Route path="/profile" element={<ProfilePage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/login" element={<LoginPage />} />
            </Routes>
          </PageLayout>
        </Router>
      </RecipeProvider>
    </AuthProvider>
  );
};

export default App;
