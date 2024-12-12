const express = require("express");
const Recipe = require("../models/Recipe");
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