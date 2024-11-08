const express = require('express');
const Favourites = require('../models/Favourites');
const router = express.Router();

router.get('/:userId', async (req, res) => {
  try {
    const favourites = await Favourites.findOne({ userId: req.params.userId }).populate('recipeIds');
    res.json(favourites);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

router.post('/:userId', async (req, res) => {
  try {
    const { recipeId } = req.body;
    let favourites = await Favourites.findOne({ userId: req.params.userId });
    if (!favourites) {
      favourites = new Favourites({ userId: req.params.userId, recipeIds: [recipeId] });
    } else {
      favourites.recipeIds.push(recipeId);
    }
    await favourites.save();
    res.status(201).json(favourites);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

module.exports = router;
