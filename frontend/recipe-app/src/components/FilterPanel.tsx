import React, { useState } from "react";

interface Filter {
  meal: string;
  difficulty: string;
  timeToPrepare: string;
}

interface FilterPanelProps {
  onFilterChange: (filters: Filter) => void;
}

const FilterPanel: React.FC<FilterPanelProps> = ({ onFilterChange }) => {
  const [filters, setFilters] = useState<Filter>({
    meal: "",
    difficulty: "",
    timeToPrepare: "",
  });

  const handleFilterChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFilters((prev) => {
      const updatedFilters = { ...prev, [name]: value };
      onFilterChange(updatedFilters);
      return updatedFilters;
    });
  };

  return (
    <div className="filter-panel">
      <select name="meal" value={filters.meal} onChange={handleFilterChange}>
        <option value="">Meal</option>
        <option value="breakfast">Breakfast</option>
        <option value="lunch">Lunch</option>
        <option value="dinner">Dinner</option>
      </select>
      <select name="difficulty" value={filters.difficulty} onChange={handleFilterChange}>
        <option value="">Difficulty</option>
        <option value="easy">Easy</option>
        <option value="medium">Medium</option>
        <option value="hard">Hard</option>
      </select>
      <select name="timeToPrepare" value={filters.timeToPrepare} onChange={handleFilterChange}>
        <option value="">Time to prepare</option>
        <option value="15">Less than 15 minutes</option>
        <option value="30">Less than 30 minutes</option>
        <option value="60">Less than 60 minutes</option>
      </select>
      <button onClick={() => onFilterChange(filters)}>Show</button>
    </div>
  );
};

export default FilterPanel;
