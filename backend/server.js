const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");

const userRoutes = require("./routes/user");
const recipeRoutes = require("./routes/recipe");

const app = express();
const PORT = 3000;

app.use(bodyParser.json());


const cors = require('cors');
app.use(cors());

mongoose.connect("mongodb+srv://dbUser:strong_pass@cluster0.jbm2x.mongodb.net/", {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

app.use("/user", userRoutes);
app.use("/recipes", recipeRoutes);

app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
