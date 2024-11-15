package com.battlecity.battle_city_backend;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BattleCityBackendApplication implements CommandLineRunner {

	@Autowired
	private SocketIOServer socketIOServer;

	public static void main(String[] args) {
		SpringApplication.run(BattleCityBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		socketIOServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> socketIOServer.stop()));
	}
}
