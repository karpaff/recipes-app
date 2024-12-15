import React, { createContext, useState, useEffect, ReactNode } from "react";
import axios from "axios";
import { Recipe } from "../types";

interface RecipeContextProps {
  recipes: Recipe[];
  fetchAll: () => void;
  toggleFavorite: (id: string) => void;
}

export const RecipeContext = createContext<RecipeContextProps | null>(null);

export const RecipeProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [recipes, setRecipes] = useState<Recipe[]>([]);

  const fetchAll = async () => {
    try {
      const response = await axios.get("http://77.221.155.11:3000/recipes");
      const fetchedRecipes = response.data.map((recipe: any) => ({
        ...recipe,
        isFavorite: false, 
      }));
      setRecipes(fetchedRecipes);
    } catch (error) {
      console.error("Failed to fetch recipes:", error);
    }
  };

  const toggleFavorite = (id: string) => {
    setRecipes((prevRecipes) =>
      prevRecipes.map((recipe) =>
        recipe.id === id ? { ...recipe, isFavorite: !recipe.isFavourite } : recipe
      )
    );
  };

  useEffect(() => {
    fetchAll();
  }, []);

  return (
    <RecipeContext.Provider value={{ recipes, fetchAll, toggleFavorite }}>
      {children}
    </RecipeContext.Provider>
  );
};
