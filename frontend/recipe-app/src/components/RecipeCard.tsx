import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./RecipeCard.css";

interface RecipeCardProps {
  id: string;
  picture: string;
  name: string;
  description: string;
  isFavorite?: boolean;
}

const RecipeCard: React.FC<RecipeCardProps> = ({ id, picture, name, description, isFavorite = false }) => {
  const [favorite, setFavorite] = useState<boolean>(isFavorite);
  const navigate = useNavigate();

  const getAuthToken = () => {
    return localStorage.getItem("token");
  };

  const toggleFavorite = async (event: React.MouseEvent<HTMLButtonElement>) => {
    // Останавливаем всплытие события, чтобы клик не обрабатывался как клик по карточке
    event.stopPropagation();

    try {
      const token = getAuthToken();
      if (!token) {
        console.error("Authorization token is missing");
        return;
      }

      if (favorite) {
        // Удаление из избранного
        await axios.delete(`http://77.221.155.11:3000/user/favourites?recipeId=${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      } else {
        // Добавление в избранное
        await axios.post(
          "http://77.221.155.11:3000/user/favourites",
          { recipeId: id },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
      }

      setFavorite(!favorite); // Переключение состояния избранного
    } catch (error) {
      console.error("Failed to update favorite status", error);
    }
  };

  const handleCardClick = () => {
    navigate(`/recipes/${id}`); // Переход на страницу рецепта
  };

  return (
    <div className="recipe-cards" onClick={handleCardClick}>
  <button
    className="recipe-cards__favorite-button"
    onClick={toggleFavorite} // Обработка клика по звёздочке
  >
    {favorite ? "★" : "☆"}
  </button>

  <div className="recipe-cards__content">
    <img src={picture} className="recipe-cards__image" alt="Recipe" />
    <div className="recipe-cards__text">
      <h2 className="recipe-cards__title">{name}</h2>
      <p className="recipe-cards__description">{description}</p>
    </div>
  </div>
</div>
  );
};

export default RecipeCard;
