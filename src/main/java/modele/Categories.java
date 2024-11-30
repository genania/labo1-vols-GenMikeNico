package modele;

public enum Categories {
    BAS_PRIX {
        @Override
        public String toString() {
            return "Bas Prix";
        }

        @Override
        public int toInt() {
            return 0;
        }
    },
    CHARTER {
        @Override
        public String toString() {
            return "Charter";
        }

        @Override
        public int toInt() {
            return 1;
        }
    },
    PRIVE {
        @Override
        public String toString() {
            return "Privé";
        }

        @Override
        public int toInt() {
            return 2;
        }
    },
    REGULIER {
        @Override
        public String toString() {
            return "Régulier";
        }

        @Override
        public int toInt() {
            return 3;
        }
    };

    // Méthode abstraite pour forcer chaque valeur de l'énumération à implémenter
    // toInt()
    public abstract int toInt();

    public static Categories convertir(int categorieInt) throws IllegalArgumentException {
        switch (categorieInt) {
            case 0:
                return Categories.BAS_PRIX;
            case 1:
                return Categories.CHARTER;
            case 2:
                return Categories.PRIVE;
            case 3:
                return Categories.REGULIER;
            default:
                throw new IllegalArgumentException("Option invalide. Les catégories ce situe entre 0 et 3");
        }
    }

}
