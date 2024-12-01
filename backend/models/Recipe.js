const mongoose = require("mongoose");

const recipeSchema = new mongoose.Schema({
  picture: String,
  name: String,
  description: String,
  createdAt: Date,
  timeToPrepare: Number,
  ingredients: [{ Name: String, Quantity: String }],
  instructions: [String],
  difficulty: String,
});

module.exports = mongoose.model("Recipe", recipeSchema);
