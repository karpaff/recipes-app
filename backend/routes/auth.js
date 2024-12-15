// Script 1: User Authentication and Management
const express = require("express");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcryptjs");
const User = require("../models/User");

const router = express.Router();
const SECRET_KEY = "4Yh3$9dxWZQkG+8_pRkCxT$n8yK#wA2Lx!k$eC&G1Y8";

// User registration
router.post("/register", async (req, res) => {
  const { login, password, role } = req.body;
  if (!login || !password) {
    return res.status(400).json({ message: "Login and password are required" });
  }
  const hashedPassword = await bcrypt.hash(password, 10);
  const newUser = new User({ login, password: hashedPassword, role });
  await newUser.save();
  res.status(201).json({ message: "User registered successfully" });
});

// User login
router.post("/login", async (req, res) => {
  const { login, password } = req.body;
  const user = await User.findOne({ login });
  if (!user || !(await bcrypt.compare(password, user.password))) {
    return res.status(401).json({ message: "Invalid credentials" });
  }
  const token = jwt.sign({ id: user._id, role: user.role }, SECRET_KEY, { expiresIn: "10h" });
  res.json({ token, user: { id: user._id, login: user.login, role: user.role } });
});

module.exports = router;
