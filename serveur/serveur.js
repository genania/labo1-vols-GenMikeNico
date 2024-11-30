const express = require("express");
const fs = require("fs");
const path = require("path");
const cors = require("cors");

const app = express();
const PORT = 3000;
const cheminFichierVols = path.join(__dirname, "donnees", "vols.json");

// Middleware
app.use(cors());
app.use(express.json());

// Récupérer la liste des cocktails
app.get("/vols", (req, res) => {
  res.sendFile(cheminFichierVols);
  res.status(200);
});

app.post("/vols", (req, res) => {
  try {
    // Convertir les données reçues en JSON
    const volsJson = JSON.stringify(req.body, null, 2);

    // Écrire les données dans le fichier
    fs.writeFileSync(cheminFichierVols, volsJson);

    // Retourner un code 200 avec un message de confirmation
    res.status(200).json({ message: "Vols ajoutés avec succès !" });
  } catch (error) {
    // En cas d'erreur, retourner un code 500 (Internal Server Error)
    res
      .status(500)
      .json({ message: "Erreur lors de l'enregistrement des vols.", error: error.message });
  }
});

// Démarrer le serveur
app.listen(PORT, () => {
  console.log(`Serveur lancé sur http://localhost:${PORT}`);
});
