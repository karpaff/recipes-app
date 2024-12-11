# Базовый образ с Tomcat
FROM tomcat:9.0

# Устанавливаем Groovy и необходимые утилиты
RUN apt-get update && \
    apt-get install -y groovy curl && \
    apt-get clean

# Копируем JAR-зависимости
COPY lib/*.jar /usr/local/tomcat/lib/

# Копируем приложение
COPY GroovyServlet.groovy /usr/local/tomcat/webapps/ROOT/GroovyServlet.groovy
COPY web.xml /usr/local/tomcat/webapps/ROOT/WEB-INF/web.xml

# Указываем порт
EXPOSE 8080

# Запускаем Tomcat
CMD ["catalina.sh", "run"]
