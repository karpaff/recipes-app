export interface Ingredient {
    Name: string;
    Quantity: string;
  }
  
  export interface Recipe {
    id: string; // Mongoose автоматически создает _id, преобразуем в строку
    picture: string;
    name: string;
    description: string;
    createdAt: Date;
    timeToPrepare: number;
    ingredients: Ingredient[];
    instructions: string[];
    difficulty: string;
    isFavourite?: boolean; // Дополнительное поле для избранного
  }
  