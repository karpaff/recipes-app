const mongoose = require("mongoose");

const userSchema = new mongoose.Schema({
  login: String,
  password: String,
  role: { type: String, enum: ["guest", "user"], default: "guest" },
});

module.exports = mongoose.model("User", userSchema);
