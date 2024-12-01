const mongoose = require("mongoose");

const favouriteSchema = new mongoose.Schema({
  userId: mongoose.Types.ObjectId,
  recipeIds: [mongoose.Types.ObjectId],
});

module.exports = mongoose.model("Favourite", favouriteSchema);
