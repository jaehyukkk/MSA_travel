from flask import Flask, request, jsonify
import time
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium import webdriver
from selenium.webdriver.chrome.service import Service

app = Flask(__name__)


class Category:
    def __init__(self, name, airports):
        self.name = name
        self.airports = airports

    def to_dict(self):
        return {
            'name': self.name,
            'airports': [info.to_dict() for info in self.airports]
        }


class Airport:
    def __init__(self, location, code, name):
        self.location = location
        self.code = code
        self.name = name

    def to_dict(self):
        return {
            'location': self.location,
            'code': self.code,
            'name': self.name
        }


class Flight:
    def __init__(self, airline_name, price, start_route, end_route):
        self.airline_name = airline_name
        self.price = price
        self.start_route = start_route
        self.end_route = end_route

    def to_dict(self):
        return {
            'airline_name': self.airline_name,
            'price': self.price,
            'start_route': self.start_route,
            'end_route': self.end_route
        }


def get_webdriver():
    # WebDriver 설정을 함수로 분리하여 재사용 가능하게 합니다.
    return webdriver.Chrome()


@app.route('/api/flights', methods=['GET'])
def get_flights():
    start_code = request.args.get('startCode')
    end_code = request.args.get('endCode')
    date = request.args.get('date')
    adult = request.args.get('adult')

    if not (start_code and end_code and date and adult):
        return jsonify({'error': 'Missing required parameters'}), 400

    browser_url = f"https://flight.naver.com/flights/international/{start_code}-{end_code}-{date}?adult={adult}&fareType=Y"
    driver = get_webdriver()
    flights = []

    try:
        driver.get(browser_url)
        time.sleep(6)
        parent_div_elements = driver.find_elements(By.XPATH, "//div[contains(@class, 'indivisual_IndivisualItem')]")

        for parent_element in parent_div_elements:
            airline_name = ""
            price = ""
            start_route = ""
            end_route = ""

            airline_name_element = parent_element.find_element(By.XPATH, ".//*[contains(@class, 'airline_name')]")
            route_airport_elements = parent_element.find_elements(By.XPATH, ".//*[contains(@class, 'route_airport')]")
            item_num = parent_element.find_element(By.XPATH, ".//*[contains(@class, 'item_num')]")
            airline_name = airline_name_element.text
            price = item_num.text
            for i, route_airport_element in enumerate(route_airport_elements):
                route_time = route_airport_element.find_element(By.XPATH, ".//*[contains(@class, 'route_time')]")
                if i == 0:
                    start_route = route_time.text
                else:
                    end_route = route_time.text

            flights.append(Flight(airline_name, price, start_route, end_route))
            if len(flights) == 100:
                break

    finally:
        driver.quit()
        return jsonify([flight.to_dict() for flight in flights])


@app.route('/api/categoriess', methods=['GET'])
def get_categories():
    browser_url = "https://flight.naver.com/flights/international/SEL-KIX-20240814?adult=1&fareType=Y"
    driver = get_webdriver()
    airports = []

    try:
        driver.get(browser_url)
        time.sleep(5)
        dropdown_btn = driver.find_element(By.XPATH, "//button[contains(@class, 'tabContent_route')]")
        dropdown_btn.click()
        time.sleep(0.5)
        section = driver.find_element(By.CSS_SELECTOR, "section.section > section.section")
        buttons = section.find_elements(By.CSS_SELECTOR, "button")

        for i, button in enumerate(buttons):
            airports.append(Category(button.text, []))
            button.click()

        autocomplete_list = section.find_elements(By.XPATH, "//div[contains(@class, 'autocomplete_list')]")

        for i, autocomplete in enumerate(autocomplete_list):
            airport_list = autocomplete.find_elements(By.CSS_SELECTOR, "button")
            for ap in airport_list:
                location = ap.find_element(By.CSS_SELECTOR, "i[class*='autocomplete_location']")
                code = ap.find_element(By.CSS_SELECTOR, "i[class*='autocomplete_code']")
                name = ap.find_element(By.CSS_SELECTOR, "span[class*='autocomplete_airport']")
                print(location.text.split(',')[0], code.text, name.text)
                airports[i].airports.append(Airport(location.text.split(',')[0], code.text, name.text))
    finally:
        driver.quit()
        return jsonify([airport.to_dict() for airport in airports])


# @app.route('/api/airport/info', methods=['GET'])
@app.route('/api/categories', methods=['GET'])
def get_airport_info():
    options = Options()
    # 새로운 사용자 데이터 디렉터리 생성
    options.add_argument("--incognito")  # 시크릿 모드로 시작
    options.add_argument("--disable-cache")  # 캐시 비활성화
    options.add_argument("--disable-application-cache")  # 애플리케이션 캐시 비활성화
    options.add_argument("--disable-extensions")  # 확장 프로그램 비활성화
    options.add_argument("--disable-dev-shm-usage")  # 메모리 공유 사용 안 함
    options.add_argument("--no-sandbox")  # 샌드박스 비활성화

    browser_url = "https://flight.naver.com/flights/international/SEL-KIX-20240814?adult=1&fareType=Y"
    driver = webdriver.Chrome(service=Service(), options=options)
    driver.get(browser_url)
    airports = []
    try:
        time.sleep(5)
        dropdown_btn = driver.find_element(By.XPATH, "//button[contains(@class, 'tabContent_route')]")
        dropdown_btn.click()
        time.sleep(1)
        section = driver.find_element(By.CSS_SELECTOR, "section.section > section.section")
        buttons = section.find_elements(By.CSS_SELECTOR, "button")

        for i, button in enumerate(buttons):
            airports.append(Category(button.text, []))
            driver.execute_script("arguments[0].click();", button)
        # autocomplete_list = section.find_elements(By.CSS_SELECTOR, "//div[contains(@class, 'autocomplete_list')]")
        autocomplete_list = section.find_elements(By.CSS_SELECTOR, "div[class*='autocomplete_list']")

        for i, autocomplete in enumerate(autocomplete_list):
            airport_list = autocomplete.find_elements(By.CSS_SELECTOR, "button")
            for j, ap in enumerate(airport_list):
                location = ap.find_element(By.CSS_SELECTOR, "i[class*='autocomplete_location']")
                code = ap.find_element(By.CSS_SELECTOR, "i[class*='autocomplete_code']")
                autocomplete_airports = ap.find_elements(By.CSS_SELECTOR, "i[class*='autocomplete_airport']")
                name = autocomplete_airports[0].text if autocomplete_airports else None
                airports[i].airports.append(Airport(location.text.split(',')[0], code.text, name))
    except Exception as e:
        print(e)
    finally:
        # driver.quit()

        # Airport 객체들을 JSON으로 변환하여 출력
        return jsonify([airport.to_dict() for airport in airports])
        # return jsonify()


if __name__ == '__main__':
    app.run(debug=True)
