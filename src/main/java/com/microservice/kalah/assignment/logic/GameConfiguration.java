package com.microservice.kalah.assignment.logic;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Setter
@Getter
public class GameConfiguration {

	private static final int DEFAULT_NUMBER_OF_PITS = 7;

	private static final int DEFAULT_NUMBER_OF_INITIAL_STONES = 6;

	private static GameConfiguration instance;

	private Integer pitsCount;

	private Integer initialStones;

	public static GameConfiguration getInstance() {
		if (instance == null) {
			instance = new GameConfiguration();
		}
		return instance;
	}

	private GameConfiguration() {
		try {
			Properties properties = loadProperties("application.properties");
			pitsCount = Integer.valueOf(properties.getProperty("logic.pits.count"));
			initialStones = Integer.valueOf(properties.getProperty("logic.stones.count"));
		}
		catch (Exception e) {
			this.pitsCount = DEFAULT_NUMBER_OF_PITS;
			this.initialStones = DEFAULT_NUMBER_OF_INITIAL_STONES;
		}
	}


	private Properties loadProperties(String resourceFileName) throws IOException {
		Properties configuration = new Properties();
		InputStream inputStream = GameConfiguration.class
				.getClassLoader()
				.getResourceAsStream(resourceFileName);
		configuration.load(inputStream);
		inputStream.close();
		return configuration;
	}
}
