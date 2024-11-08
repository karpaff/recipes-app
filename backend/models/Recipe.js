const mongoose = require('mongoose');

const ingredientSchema = new mongoose.Schema({
  name: String,
  quantity: String,
});

const recipeSchema = new mongoose.Schema({
  picture: String,
  name: { type: String, required: true },
  description: String,
  createdAt: { type: Date, default: Date.now },
  timeToPrepare: Number,
  ingredients: [ingredientSchema],
  instructions: [String],
  difficulty: { type: String, enum: ['easy', 'medium', 'hard'] }
});

module.exports = mongoose.model('Recipe', recipeSchema);
