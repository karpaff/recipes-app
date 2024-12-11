package com.example.GroovyServlet

import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import java.io.PrintWriter

// URL первого сервиса
String serviceUrl = System.getenv("SERVICE_URL") ?: "http://localhost:8080/recipes"

// Параметры запроса
String token = request.getHeader("Authorization")
String exportFormat = request.getParameter("format") ?: "csv" // csv или xml

// Обращение к первому сервису для получения рецептов
def getRecipes(String url, String token) {
    def connection = new URL(url).openConnection()
    if (token) {
        connection.setRequestProperty("Authorization", token)
    }
    connection.setRequestMethod("GET")

    def responseCode = connection.responseCode
    if (responseCode == 200) {
        def responseText = connection.inputStream.text
        return new JsonSlurper().parseText(responseText)
    } else {
        throw new Exception("Failed to fetch recipes. HTTP response code: $responseCode")
    }
}

// Экспорт данных в CSV
void exportToCSV(List recipes, PrintWriter writer) {
    writer.println("id,name,description,picture")
    recipes.each { recipe ->
        writer.println("${recipe.id},${recipe.name},${recipe.description},${recipe.picture}")
    }
}

// Экспорт данных в XML
void exportToXML(List recipes, PrintWriter writer) {
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
    writer.println("<recipes>")
    recipes.each { recipe ->
        writer.println("  <recipe>")
        writer.println("    <id>${recipe.id}</id>")
        writer.println("    <name>${recipe.name}</name>")
        writer.println("    <description>${recipe.description}</description>")
        writer.println("    <picture>${recipe.picture}</picture>")
        writer.println("  </recipe>")
    }
    writer.println("</recipes>")
}

response.setContentType("application/json")
response.setCharacterEncoding("UTF-8")

try {
    // Получение рецептов
    def recipes = getRecipes(serviceUrl, token)

    if (recipes && recipes.size() > 0) {
        // Выбор формата экспорта
        if (exportFormat.toLowerCase() == "csv") {
            response.setContentType("text/csv")
            response.setHeader("Content-Disposition", "attachment; filename=recipes.csv")
            exportToCSV(recipes, response.writer)
        } else if (exportFormat.toLowerCase() == "xml") {
            response.setContentType("application/xml")
            response.setHeader("Content-Disposition", "attachment; filename=recipes.xml")
            exportToXML(recipes, response.writer)
        } else {
            throw new Exception("Unsupported export format: $exportFormat")
        }
    } else {
        response.setStatus(404)
        response.writer.println(JsonOutput.toJson([error: "No recipes found"]))
    }
} catch (Exception e) {
    response.setStatus(500)
    response.writer.println(JsonOutput.toJson([error: e.message]))
}
