import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./RecipePage.css";

interface Ingredient {
  name: string;
  quantity: string;
  _id: string;
}

interface Recipe {
  _id: string;
  picture: string;
  name: string;
  description: string;
  createdAt: string;
  timeToPrepare: number;
  ingredients: Ingredient[];
  instructions: string[];
  difficulty: string;
}

const RecipePage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [recipe, setRecipe] = useState<Recipe | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchRecipe = async () => {
      try {
        const response = await axios.get<Recipe>(`http://77.221.155.11:3000/recipes/${id}`);
        setRecipe(response.data);
      } catch (err) {
        setError("Failed to load recipe");
      } finally {
        setLoading(false);
      }
    };

    fetchRecipe();
  }, [id]);


  if (loading) return <p>Loading...</p>;
  if (error) return <p>{error}</p>;

  if (!recipe) return <p>Recipe not found</p>;

  return (
    <div className="recipe-card">
    
    <div className="top-content">
    <img
      src={recipe.picture}
      alt={recipe.name}
      className="recipe-image"
    />
      <div className="ingredients">
        <h3>Ingredients</h3>
        <ul>
          {recipe.ingredients.map((ingredient) => (
            <li key={ingredient._id}>
              {ingredient.name} - {ingredient.quantity}
            </li>
          ))}
        </ul>
      </div>
      
    </div>
    <div className="bottom-content">
      <h1>{recipe.name}</h1>
      <div className="details">
        <p className="meta">Created at: {new Date(recipe.createdAt).toLocaleDateString()}</p>
        <p><strong>Description:</strong> {recipe.description}</p>
        <p><strong>Time to prepare:</strong> {recipe.timeToPrepare} minutes</p>
        <p><strong>Difficulty:</strong> {recipe.difficulty}</p>
      </div>
      <div className="instructions">
        <h3>Instructions</h3>
        <ol>
          {recipe.instructions.map((step, index) => (
            <li key={index}>{step}</li>
          ))}
        </ol>
      </div>
    </div>
  </div>
  

  
  );
};

export default RecipePage;
