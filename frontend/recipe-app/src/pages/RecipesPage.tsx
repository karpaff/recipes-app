import React, { useContext } from "react";
import RecipeList from "../components/RecipeList";
import FilterPanel from "../components/FilterPanel";
import { RecipeContext } from "../context/RecipeContext";

const RecipesPage: React.FC = () => {
  const recipeContext = useContext(RecipeContext);

  if (!recipeContext) return null;

  const { fetchAll } = recipeContext;

  const handleFilterChange = (filters: any) => {
    console.log("Filters:", filters); // Реализуйте логику фильтрации здесь
    fetchAll(); // Пример вызова функции для обновления рецептов
  };

  return (
    <div className="recipes-page">
      <FilterPanel onFilterChange={handleFilterChange} />
      <RecipeList />
    </div>
  );
};

export default RecipesPage;
