// Script 1: User Authentication and Management
const express = require("express");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const Favourite = require("../models/Favourite");
const Recipe = require("../models/Recipe");

const router = express.Router();
const SECRET_KEY = "4Yh3$9dxWZQkG+8_pRkCxT$n8yK#wA2Lx!k$eC&G1Y8";

// Get user favourites
router.get("/favourites", async (req, res) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return res.status(401).json({ message: "Authorization token is missing or invalid" });
  }

  const token = authHeader.split(" ")[1];
  try {
    const decoded = jwt.verify(token, SECRET_KEY);
    const favourites = await Favourite.findOne({ userId: decoded.id });

    if (!favourites || favourites.recipeIds.length === 0) {
      return res.status(404).json({ message: "No favourites found for this user" });
    }

    const recipes = await Recipe.find(
      { _id: { $in: favourites.recipeIds } },
      "id name description picture"
    );
    res.json(recipes);
  } catch (error) {
    res.status(401).json({ message: "Invalid or expired token", error });
  }
});

// Add a recipe to favourites
router.post("/favourites", async (req, res) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return res.status(401).json({ message: "Authorization token is missing or invalid" });
  }

  const token = authHeader.split(" ")[1];
  try {
    const decoded = jwt.verify(token, SECRET_KEY);
    const { recipeId } = req.body;

    if (!recipeId) {
      return res.status(400).json({ message: "Recipe ID is required" });
    }

    const recipe = await Recipe.findById(recipeId);
    if (!recipe) {
      return res.status(404).json({ message: "Recipe not found" });
    }

    let favourites = await Favourite.findOne({ userId: decoded.id });
    if (!favourites) {
      favourites = new Favourite({ userId: decoded.id, recipeIds: [] });
    }

    if (!favourites.recipeIds.includes(recipeId)) {
      favourites.recipeIds.push(recipeId);
      await favourites.save();
      res.status(200).json({ message: "Recipe added to favourites" });
    } else {
      res.status(400).json({ message: "Recipe is already in favourites" });
    }
  } catch (error) {
    res.status(401).json({ message: "Invalid or expired token", error });
  }
});

// Remove a recipe from favourites
router.delete("/favourites", async (req, res) => {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith("Bearer ")) {
    return res.status(401).json({ message: "Authorization token is missing or invalid" });
  }

  const token = authHeader.split(" ")[1];
  try {
    const decoded = jwt.verify(token, SECRET_KEY);
    const recipeId = req.query.recipeId;

    if (!recipeId) {
      return res.status(400).json({ message: "Recipe ID is required" });
    }

    let favourites = await Favourite.findOne({ userId: decoded.id });
    if (!favourites) {
      return res.status(404).json({ message: "No favourites found for this user" });
    }

    const index = favourites.recipeIds.findIndex((id) => id.toString() === recipeId);
    if (index === -1) {
      return res.status(404).json({ message: "Recipe not found in favourites" });
    }

    favourites.recipeIds.splice(index, 1);
    await favourites.save();

    res.status(200).json({ message: "Recipe removed from favourites" });
  } catch (error) {
    res.status(401).json({ message: "Invalid or expired token", error });
  }
});

module.exports = router;
