const express = require("express");
const Recipe = require("../models/Recipe");
const Favourite = require("../models/Favourite");
const jwt = require("jsonwebtoken");

const router = express.Router();
const SECRET_KEY = "4Yh3$9dxWZQkG+8_pRkCxT$n8yK#wA2Lx!k$eC&G1Y8";

const authenticate = (req, res, next) => {
  const authHeader = req.headers.authorization;

  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return res.status(401).json({ message: "Authorization token is missing or invalid" });
  }

  const token = authHeader.split(" ")[1];
  try {
    req.user = jwt.verify(token, SECRET_KEY);
    next();
  } catch (error) {
    res.status(401).json({ message: "Invalid or expired token" });
  }
};

router.get("/", async (req, res) => {
  try {
    const token = req.headers.authorization?.split(" ")[1];
    let userId = null;

    if (token) {
      try {
        const decoded = jwt.verify(token, SECRET_KEY);
        userId = decoded.id;
      } catch (err) {
        return res.status(401).json({ message: "Invalid or expired token" });
      }
    }

    const recipes = await Recipe.find({}, "id name description picture");

    let favourites = [];
    if (userId) {
      const favouriteData = await Favourite.findOne({ userId });
      if (favouriteData) {
        favourites = favouriteData.recipeIds.map((id) => id.toString());
      }
    }

    const enrichedRecipes = recipes.map((recipe) => ({
      ...recipe._doc, 
      isFavourite: favourites.includes(recipe._id.toString()),
    }));

    res.json(enrichedRecipes);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: "Error fetching recipes" });
  }
});

router.get("/:id", async (req, res) => {
  const { id } = req.params;
  console.log(id);
  try {
    const recipe = await Recipe.findById(id);

    if (!recipe) {
      return res.status(404).json({ message: "Recipe not found" });
    }

    res.json(recipe);
  } catch (error) {
    console.error("Error fetching recipe:", error);
    res.status(500).json({ message: "Error fetching recipe", error });
  }
});

router.post("/create", authenticate, async (req, res) => {
  const recipeData = req.body;

  const newRecipe = new Recipe(recipeData);
  await newRecipe.save();

  res.status(201).json(newRecipe);
});

router.delete("/:id", authenticate, async (req, res) => {
  const { id } = req.params;
  await Recipe.findByIdAndDelete(id);
  res.status(200).json({ message: "Recipe deleted successfully" });
});

module.exports = router;
