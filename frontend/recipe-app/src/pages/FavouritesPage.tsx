import React, { useContext, useEffect, useState } from "react";
import RecipeList from "../components/RecipeList";
import { RecipeContext } from "../context/RecipeContext";
import "./FavoritesPage.css";

const FavouritesPage: React.FC = () => {
  const recipeContext = useContext(RecipeContext);

  if (!recipeContext) return null;
  
  const [recipes, setRecipes] = useState<any[]>([]);
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const fetchRecipes = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await fetch("http://77.221.155.11:3000/user/favourites", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          ...(token && { Authorization: `Bearer ${token}` }), 
        },
      });

      if (!response.ok) {
        throw new Error("Failed to load recipes");
      }

      const data = await response.json();
      const updatedData = data.map((item: any) => ({
        ...item,
        isFavourite: true
      }));
      setRecipes(updatedData);
    } catch (err) {
      setError("Failed to load recipes");
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

  return (
    <div className="recipes-page">
      <input
        className="search"
        type="text"
        placeholder="Search recipe..."
        value={searchQuery}
        onChange={handleSearchChange}
      />
      {loading ? (
        <p>Loading recipes...</p>
      ) : error ? (
        <p>{error}</p>
      ) : (
        <RecipeList recipes={filteredRecipes} />
      )}
    </div>
  );
};

export default FavouritesPage;
