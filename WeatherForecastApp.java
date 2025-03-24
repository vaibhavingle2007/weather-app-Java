import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherForecastApp extends JFrame {
    private JTextField cityField;
    private JLabel weatherLabel;
    private JLabel temperatureLabel;
    private JLabel humidityLabel;

    public WeatherForecastApp() {
        setTitle("Weather Forecast Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel cityLabel = new JLabel("Enter City:");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        inputPanel.add(cityLabel);

        cityField = new JTextField(20);
        cityField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(cityField);

        JButton fetchButton = new JButton("Get Weather");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 16));
        fetchButton.setBackground(new Color(52, 152, 219));
        fetchButton.setForeground(Color.WHITE);
        fetchButton.setFocusPainted(false);
        fetchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchWeather();
            }
        });
        inputPanel.add(fetchButton);

        add(inputPanel, BorderLayout.NORTH);

        // Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(3, 1, 10, 10));
        resultPanel.setBackground(new Color(255, 255, 255));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        weatherLabel = new JLabel("Weather: ");
        weatherLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultPanel.add(weatherLabel);

        temperatureLabel = new JLabel("Temperature: ");
        temperatureLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultPanel.add(temperatureLabel);

        humidityLabel = new JLabel("Humidity: ");
        humidityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultPanel.add(humidityLabel);

        add(resultPanel, BorderLayout.CENTER);
    }

    private void fetchWeather() {
        String city = cityField.getText();
        String apiKey = "3611c6d6373b8ff0bef3287831a1d98e"; // Replace with your actual OpenWeatherMap API key
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            connection.disconnect();

            JSONObject jsonObject = new JSONObject(content.toString());
            String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            double temperature = jsonObject.getJSONObject("main").getDouble("temp");
            double humidity = jsonObject.getJSONObject("main").getDouble("humidity");

            weatherLabel.setText("Weather: " + weather);
            temperatureLabel.setText("Temperature: " + temperature + "°C");
            humidityLabel.setText("Humidity: " + humidity + "%");
        } catch (Exception e) {
            weatherLabel.setText("Error fetching weather data.");
            temperatureLabel.setText("");
            humidityLabel.setText("");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherForecastApp().setVisible(true);
            }
        });
    }
}
