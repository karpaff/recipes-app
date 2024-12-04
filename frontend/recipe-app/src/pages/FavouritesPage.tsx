import React, { useEffect, useState } from "react";
import axios from "axios";
import RecipeCard from "../components/RecipeCard";

interface Recipe {
  _id: string;
  picture: string;
  name: string;
  description: string;
}

const FavouritesPage: React.FC = () => {
  const [recipes, setRecipes] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchFavourites = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://77.221.155.11:3000/user/favourites", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setRecipes(response.data);
      } catch (err) {
        setError("Failed to load favourites");
      } finally {
        setLoading(false);
      }
    };

    fetchFavourites();
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", padding: "20px" }}>
      {recipes.map((recipe) => (
        <RecipeCard
          key={recipe._id}
          id={recipe._id}
          picture={recipe.picture}
          name={recipe.name}
          description={recipe.description}
          isFavorite={true}
        />
      ))}
    </div>
  );
};

export default FavouritesPage;
