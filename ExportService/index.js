const express = require("express");
const { Parser } = require("json2csv");
const http = require("http");

const app = express();
const PORT = 3002;
const FIRST_SERVICE_HOST = "77.221.155.11";
const FIRST_SERVICE_PORT = 3000;

app.use(express.json());

// Функция для отправки запросов к первому сервису
function makeRequest(path, method, token) {
    return new Promise((resolve, reject) => {
        const options = {
            hostname: FIRST_SERVICE_HOST,
            port: FIRST_SERVICE_PORT,
            path,
            method,
            headers: token ? { Authorization: `Bearer ${token}` } : {},
        };

        const req = http.request(options, (res) => {
            let data = "";

            res.on("data", (chunk) => {
                data += chunk;
            });

            res.on("end", () => {
                if (res.statusCode >= 200 && res.statusCode < 300) {
                    resolve(JSON.parse(data));
                } else {
                    reject(new Error(`Request failed with status code ${res.statusCode}`));
                }
            });
        });

        req.on("error", (error) => {
            reject(error);
        });

        req.end();
    });
}

// Получить список всех рецептов и экспортировать в CSV
app.get("/recipes/export", async (req, res) => {
    try {
        const token = req.headers.authorization?.split(" ")[1];
        const recipes = await makeRequest("/recipes", "GET", token);

        // Преобразование в CSV
        const fields = ["id", "name", "description", "picture"];
        const parser = new Parser({ fields });
        const csv = parser.parse(recipes);

        res.header("Content-Type", "text/csv");
        res.attachment("recipes.csv");
        res.send(csv);
    } catch (error) {
        console.error("Error fetching or exporting recipes:", error.message);
        res.status(500).json({ message: "Failed to export recipes." });
    }
});

// Получить избранные рецепты пользователя и экспортировать в CSV
app.get("/favourites/export", async (req, res) => {
    try {
        const token = req.headers.authorization?.split(" ")[1];

        if (!token) {
            return res.status(401).json({ message: "Authorization token is required." });
        }

        const favourites = await makeRequest("/recipes/favourites", "GET", token);

        // Преобразование в CSV
        const fields = ["id", "name", "description", "picture"];
        const parser = new Parser({ fields });
        const csv = parser.parse(favourites);

        res.header("Content-Type", "text/csv");
        res.attachment("favourites.csv");
        res.send(csv);
    } catch (error) {
        console.error("Error fetching or exporting favourites:", error.message);
        res.status(500).json({ message: "Failed to export favourites." });
    }
});

app.listen(PORT, () => {
    console.log(`Second service is running on port ${PORT}`);
});
