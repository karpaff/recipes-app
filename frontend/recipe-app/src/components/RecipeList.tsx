// src/components/RecipeList.tsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import RecipeCard from "./RecipeCard";
import "./RecipeList.css";

interface Recipe {
  isFavourite: boolean | undefined;
  _id: string;
  picture: string;
  name: string;
  description: string;
}

const RecipeList: React.FC = () => {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRecipes = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await fetch("http://77.221.155.11:3000/recipes", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            ...(token && { Authorization: `Bearer ${token}` }), // Добавляем токен в заголовок, если он есть
          },
        });
        if (!response.ok) {
          throw new Error("Не удалось загрузить рецепты");
        }

        const data = await response.json();
        setRecipes(data);
      } catch (err) {
        setError("Failed to fetch recipes.");
      } finally {
        setLoading(false);
      }
    };

    fetchRecipes();
  }, []);

  if (loading) {
    return <p>Loading recipes...</p>;
  }

  if (error) {
    return <p>{error}</p>;
  }

  return (
    <div className="recipe-list">
      {recipes.map((recipe) => (
        <RecipeCard
          key={recipe._id}
          id={recipe._id}
          picture={recipe.picture}
          name={recipe.name}
          description={recipe.description}
          isFavorite={recipe.isFavourite}
        />
      ))}
    </div>
  );
};

export default RecipeList;
