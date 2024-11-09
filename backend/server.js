const express = require('express');
const mongoose = require('mongoose');
const userRoutes = require('./routes/userRoutes');
const recipeRoutes = require('./routes/recipeRoutes');
const favouritesRoutes = require('./routes/favouritesRoutes');

const app = express();
app.use(express.json());

mongoose.connect('mongodb+srv://dbUser:strong_pass@cluster0.jbm2x.mongodb.net/')
  .then(() => console.log('MongoDB connected'))
  .catch((error) => console.error('MongoDB connection error:', error));

app.use('/users', userRoutes);
app.use('/recipes', recipeRoutes);
app.use('/favourites', favouritesRoutes);

app.listen(3000, () => console.log('Server is running on port 3000'));
