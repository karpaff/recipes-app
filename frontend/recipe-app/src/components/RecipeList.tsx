// src/components/RecipeList.tsx
import React from "react";
import RecipeCard from "./RecipeCard";
import "./RecipeList.css";

interface Recipe {
  isFavourite: boolean | undefined;
  _id: string;
  picture: string;
  name: string;
  description: string;
}

interface RecipeListProps {
  recipes: Recipe[];
}

const RecipeList: React.FC<RecipeListProps> = ({ recipes }) => {
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
