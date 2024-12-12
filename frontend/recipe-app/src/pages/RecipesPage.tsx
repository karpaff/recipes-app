import React, { useContext, useEffect, useState } from "react";
import RecipeList from "../components/RecipeList";
import FilterPanel from "../components/FilterPanel";
import { RecipeContext } from "../context/RecipeContext";
import "./RecipesPage.css";

const RecipesPage: React.FC = () => {
  const recipeContext = useContext(RecipeContext);

  if (!recipeContext) return null;

  const { fetchAll } = recipeContext;
  const [recipes, setRecipes] = useState<any[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchRecipes = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("http://77.221.155.11:3000/recipes", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          ...(token && { Authorization: `Bearer ${token}` }), // Добавляем токен, если он есть
        },
      });

      if (!response.ok) {
        throw new Error("Не удалось загрузить рецепты");
      }

      const data = await response.json();
      setRecipes(data);
    } catch (err) {
      setError("Не удалось загрузить рецепты");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRecipes();
  }, []);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(event.target.value);
  };

  const filteredRecipes = recipes.filter((recipe) =>
    recipe.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleFilterChange = (filters: any) => {
    console.log("Filters:", filters); // Реализуйте логику фильтрации здесь
    fetchAll(); // Пример вызова функции для обновления рецептов
  };

  return (
    <div className="recipes-page">
      <input
        type="text"
        placeholder="Поиск рецептов..."
        value={searchQuery}
        onChange={handleSearchChange}
      />
      {loading ? (
        <p>Загрузка рецептов...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <RecipeList recipes={filteredRecipes} />
      )}
    </div>
  );
};

export default RecipesPage;
