import json
import csv
import xml.etree.ElementTree as ET
from http.server import BaseHTTPRequestHandler, HTTPServer
import urllib.request
from urllib.parse import urlparse, parse_qs
from io import StringIO

FIRST_SERVICE_HOST = "77.221.155.11"
FIRST_SERVICE_PORT = 3000
PORT = 3002

# Функция для отправки запросов к первому сервису
def make_request(path, method, token):
    url = f"http://{FIRST_SERVICE_HOST}:{FIRST_SERVICE_PORT}{path}"
    headers = {"Authorization": f"Bearer {token}"} if token else {}
    req = urllib.request.Request(url, method=method, headers=headers)

    with urllib.request.urlopen(req) as response:
        if 200 <= response.status < 300:
            return json.load(response)
        else:
            raise Exception(f"Request failed with status code {response.status}")

# Преобразование данных в CSV
def convert_to_csv(data, fields):
    output = StringIO()
    writer = csv.DictWriter(output, fieldnames=fields)
    writer.writeheader()
    writer.writerows(data)
    return output.getvalue()

# Преобразование данных в XML
def convert_to_xml(data, root_element):
    root = ET.Element(root_element)
    for item in data:
        item_element = ET.SubElement(root, "item")
        for key, value in item.items():
            child = ET.SubElement(item_element, key)
            child.text = str(value)
    return ET.tostring(root, encoding="utf-8").decode("utf-8")

class RequestHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        parsed_path = urlparse(self.path)
        query_params = parse_qs(parsed_path.query)
        export_format = query_params.get("format", ["csv"])[0]

        if parsed_path.path == "/recipes/export":
            self.export_data("/recipes", export_format, "recipes.csv", "recipes")
        elif parsed_path.path == "/favourites/export":
            self.export_data("/recipes/favourites", export_format, "favourites.csv", "favourites")
        else:
            self.send_error(404, "Not Found")

    def export_data(self, api_path, export_format, filename, root_element):
        try:
            token = self.headers.get("Authorization", "").split(" ")[1] if "Authorization" in self.headers else None
            if not token:
                self.send_error(401, "Authorization token is required.")
                return

            data = make_request(api_path, "GET", token)
            fields = ["_id", "isFavourite", "id", "name", "description", "picture"]
            print(data)
            if export_format == "csv":
                content = convert_to_csv(data, fields)
                print(content)
                content_type = "text/csv"
            elif export_format == "xml":
                content = convert_to_xml(data, root_element)
                content_type = "application/xml"
            else:
                self.send_error(400, "Invalid format. Supported formats are 'csv' and 'xml'.")
                return

            
            self.send_response(200)
            self.send_header("Content-Type", content_type)
            self.send_header("Content-Disposition", f"attachment; filename={filename}")
            self.end_headers()
            self.wfile.write(content.encode("utf-8"))

        except Exception as e:
            self.send_error(500, str(e))

if __name__ == "__main__":
    server = HTTPServer(("", PORT), RequestHandler)
    print(f"Server running on port {PORT}")
    server.serve_forever()