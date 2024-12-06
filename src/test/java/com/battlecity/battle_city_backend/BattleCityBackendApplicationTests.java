package com.battlecity.battle_city_backend;

import com.battlecity.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

class BattleCityBackendApplicationTests {

	// Test GameRoom
	@Test
	void testCanAddPlayer() {
		GameRoom room = new GameRoom("room1", 2);
		Player player1 = new Player("1", "Player 1", "red");
		Player player2 = new Player("2", "Player 2", "blue");

		room.addPlayer(player1);
		room.addPlayer(player2);

		assertFalse(room.canPlayerJoin(), "Room should be full");
	}

	@Test
	void testAddPlayer() {
		GameRoom room = new GameRoom("room1", 2);
		Player player = new Player("1", "Player 1", "red");

		room.addPlayer(player);

		assertEquals(1, room.getPlayers().size());
		assertEquals("Player 1", room.getPlayers().get(0).getName());
	}

	@Test
	void testRemovePlayer() {
		GameRoom room = new GameRoom("room1", 2);
		Player player = new Player("1", "Player 1", "red");

		room.addPlayer(player);
		room.removePlayer("1");  // Use the renamed method

		assertEquals(0, room.getPlayers().size());
	}

	// Test Player
	@Test
	void testPlayerInitialization() {
		Player player = new Player("1", "Player One", "green");

		assertEquals("1", player.getId());
		assertEquals("Player One", player.getName());
		assertEquals("green", player.getTankColor());
	}

	@Test
	void testPlayerSetters() {
		Player player = new Player("1", "Player One", "green");

		player.setId("2");
		player.setName("Player Two");
		player.setTankColor("blue");

		assertEquals("2", player.getId());
		assertEquals("Player Two", player.getName());
		assertEquals("blue", player.getTankColor());
	}
	@Test
	void testPlayerFullConstructor() {
		Player player = new Player("1", "Player One", "green", 3, true);

		assertEquals("1", player.getId(), "ID should be '1'");
		assertEquals("Player One", player.getName(), "Name should be 'Player One'");
		assertEquals("green", player.getTankColor(), "Tank color should be 'green'");
		assertEquals(3, player.getLives(), "Lives should be 3");
		assertTrue(player.isAlive(), "Player should be alive");
	}

	@Test
	void testPlayerConstructorWithIdNameColor() {
		Player player = new Player("2", "Player Two", "blue");

		assertEquals("2", player.getId(), "ID should be '2'");
		assertEquals("Player Two", player.getName(), "Name should be 'Player Two'");
		assertEquals("blue", player.getTankColor(), "Tank color should be 'blue'");
		assertNull(player.getPosition(), "Position should be null");
		assertEquals("down", player.getDirection(), "Direction should be 'down'");
		assertTrue(player.isAlive(), "Player should be alive");
	}

	@Test
	void testSetAndGetId() {
		Player player = new Player();
		player.setId("3");

		assertEquals("3", player.getId(), "ID should be '3'");
	}

	@Test
	void testSetAndGetName() {
		Player player = new Player();
		player.setName("Player Three");

		assertEquals("Player Three", player.getName(), "Name should be 'Player Three'");
	}

	@Test
	void testSetAndGetPosition() {
		Player player = new Player();
		Position position = new Position();
		position.setX(10);
		position.setY(20);

		player.setPosition(position);

		assertNotNull(player.getPosition(), "Position should not be null");
		assertEquals(10, player.getPosition().getX(), "X position should be 10");
		assertEquals(20, player.getPosition().getY(), "Y position should be 20");
	}

	@Test
	void testSetAndGetDirection() {
		Player player = new Player();
		player.setDirection("left");

		assertEquals("left", player.getDirection(), "Direction should be 'left'");
	}

	@Test
	void testSetAndGetTankColor() {
		Player player = new Player();
		player.setTankColor("red");

		assertEquals("red", player.getTankColor(), "Tank color should be 'red'");
	}

	@Test
	void testSetAndGetLives() {
		Player player = new Player();
		player.setLives(5);

		assertEquals(5, player.getLives(), "Lives should be 5");
	}

	@Test
	void testSetAndGetAlive() {
		Player player = new Player();
		player.setAlive(false);

		assertFalse(player.isAlive(), "Player should be not alive");
	}

	@Test
	void testJsonMappingAlive() {
		Player player = new Player();
		player.setAlive(true);

		assertTrue(player.isAlive(), "Player should be alive after setting 'isAlive' to true");
	}

	@Test
	void testPlayerStatusChange() {
		Player player = new Player("4", "Player Four", "yellow", 3, true);

		player.setAlive(false);
		player.setLives(0);

		assertFalse(player.isAlive(), "Player should be dead");
		assertEquals(0, player.getLives(), "Lives should be 0");
	}

	@Test
	void testPlayerConstructorNegativeLives() {
		Player player = new Player("5", "Player Five", "purple", -1, false);

		assertEquals(-1, player.getLives(), "Lives should be -1");
		assertFalse(player.isAlive(), "Player should be not alive");
	}

	// Test PlayerShot
	@Test
	void testPlayerShotInitialization() {
		PlayerShot shot = new PlayerShot();
		shot.setPlayerId("player1");
		shot.setX(5);
		shot.setY(10);
		shot.setDirection("up");

		assertEquals("player1", shot.getPlayerId());
		assertEquals(5, shot.getX());
		assertEquals(10, shot.getY());
		assertEquals("up", shot.getDirection());
	}

	// Test PlayerMove
	@Test
	void testPlayerMoveInitialization() {
		PlayerMove move = new PlayerMove(10);
		move.setY(5);
		move.setPlayerId("player1");
		move.setDirection("right");

		assertEquals(10, move.getX());
		assertEquals(5, move.getY());
		assertEquals("player1", move.getPlayerId());
		assertEquals("right", move.getDirection());
	}

	// Test PlayerAction
	@Test
	void testPlayerActionInitialization() {
		PlayerAction action = new PlayerAction();
		action.setType("move");
		action.setPlayerId("player1");
		action.setDirection("down");
		action.setPosition(new Position());
		action.setBullet(new Bullet());

		assertEquals("move", action.getType());
		assertEquals("player1", action.getPlayerId());
		assertEquals("down", action.getDirection());
		assertNotNull(action.getPosition());
		assertNotNull(action.getBullet());
	}

	@Test
	void testPlayerActionBullet() {
		Bullet bullet = new Bullet();
		bullet.setId("bullet1");
		bullet.setX(5);
		bullet.setY(10);
		bullet.setDirection("up");
		bullet.setPlayerId("player1");

		PlayerAction action = new PlayerAction();
		action.setBullet(bullet);

		assertEquals("bullet1", action.getBullet().getId());
		assertEquals(5, action.getBullet().getX());
		assertEquals(10, action.getBullet().getY());
		assertEquals("up", action.getBullet().getDirection());
		assertEquals("player1", action.getBullet().getPlayerId());
	}

	// Test GameState
	@Test
	void testGameStateInitialization() {
		GameState gameState = new GameState();
		gameState.setType("gameStart");
		gameState.setPlayerId("player1");

		assertEquals("gameStart", gameState.getType());
		assertEquals("player1", gameState.getPlayerId());
	}

	// Test Bullet
	@Test
	void testBulletInitialization() {
		Bullet bullet = new Bullet();
		bullet.setId("bullet1");
		bullet.setX(10);
		bullet.setY(20);
		bullet.setDirection("up");
		bullet.setPlayerId("player1");

		assertEquals("bullet1", bullet.getId());
		assertEquals(10, bullet.getX());
		assertEquals(20, bullet.getY());
		assertEquals("up", bullet.getDirection());
		assertEquals("player1", bullet.getPlayerId());
	}

	@Test
	void testBulletEquality() {
		Bullet bullet1 = new Bullet();
		Bullet bullet2 = new Bullet();

		bullet1.setId("bullet3");
		bullet1.setX(30);
		bullet1.setY(40);
		bullet1.setDirection("left");
		bullet1.setPlayerId("player3");

		bullet2.setId("bullet3");
		bullet2.setX(30);
		bullet2.setY(40);
		bullet2.setDirection("left");
		bullet2.setPlayerId("player3");

		assertEquals(bullet1.getId(), bullet2.getId());
		assertEquals(bullet1.getX(), bullet2.getX());
		assertEquals(bullet1.getY(), bullet2.getY());
		assertEquals(bullet1.getDirection(), bullet2.getDirection());
		assertEquals(bullet1.getPlayerId(), bullet2.getPlayerId());
	}

	// Test Position
	@Test
	void testPositionInitialization() {
		Position position = new Position();
		position.setX(10);
		position.setY(20);

		assertEquals(10, position.getX());
		assertEquals(20, position.getY());
	}

	@Test
	void testPositionSettersAndGetters() {
		Position position = new Position();
		position.setX(30);
		position.setY(40);

		assertEquals(30, position.getX());
		assertEquals(40, position.getY());
	}

	@Test
	void testPositionDefaultValues() {
		Position position = new Position();

		assertEquals(0, position.getX());
		assertEquals(0, position.getY());
	}

	@Test
	void testPositionEquality() {
		Position position1 = new Position();
		Position position2 = new Position();

		position1.setX(50);
		position1.setY(60);

		position2.setX(50);
		position2.setY(60);

		assertEquals(position1.getX(), position2.getX());
		assertEquals(position1.getY(), position2.getY());
	}

	// Test Power
	@Test
	void testPowerInitialization() {
		Power power = new Power(3, 5, "speed");

		assertEquals(3, power.getRow());
		assertEquals(5, power.getCol());
		assertEquals("speed", power.getType());
	}

	@Test
	void testPowerSettersAndGetters() {
		Power power = new Power(3, 5, "shield");

		power.setRow(6);
		power.setCol(8);
		power.setType("extra-life");

		assertEquals(6, power.getRow());
		assertEquals(8, power.getCol());
		assertEquals("extra-life", power.getType());
	}

	@Test
	void testPowerDefaultValues() {
		Power power = new Power(0, 0, "");

		assertEquals(0, power.getRow());
		assertEquals(0, power.getCol());
		assertEquals("", power.getType());
	}

	@Test
	void testPowerEquality() {
		Power power1 = new Power(3, 5, "speed");
		Power power2 = new Power(3, 5, "speed");

		assertEquals(power1.getRow(), power2.getRow());
		assertEquals(power1.getCol(), power2.getCol());
		assertEquals(power1.getType(), power2.getType());
	}

	@Test
	void testPowerInequality() {
		Power power1 = new Power(3, 5, "speed");
		Power power2 = new Power(6, 8, "shield");

		assertNotEquals(power1.getRow(), power2.getRow());
		assertNotEquals(power1.getCol(), power2.getCol());
		assertNotEquals(power1.getType(), power2.getType());
	}

	// Test Wall
	@Test
	void testWallInitialization() {
		Position position = new Position();
		Wall wall = new Wall("wall1", position);

		assertEquals("wall1", wall.getId());
		assertEquals(position, wall.getPosition());
		assertFalse(wall.isDestroyed());
	}

	@Test
	void testWallDestroy() {
		Position position = new Position();
		Wall wall = new Wall("wall1", position);

		assertFalse(wall.isDestroyed());

		wall.destroy();

		assertTrue(wall.isDestroyed());
	}

	// Test PlayerDTO
	@Test
	void testPlayerDTOInitialization() {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setId("1");
		playerDTO.setName("Player One");
		playerDTO.setScore(100);

		Position position = new Position();
		position.setX(10);
		position.setY(20);

		playerDTO.setPosition(position);
		playerDTO.setDirection("up");
		playerDTO.setTankColor("green");
		assertEquals("1", playerDTO.getId());
		assertEquals("Player One", playerDTO.getName());
		assertEquals(100, playerDTO.getScore());
		assertNotNull(playerDTO.getPosition());
		assertEquals(10, playerDTO.getPosition().getX());
		assertEquals(20, playerDTO.getPosition().getY());
		assertEquals("up", playerDTO.getDirection());
		assertEquals("green", playerDTO.getTankColor());
	}

	@Test
	void testPlayerDTOSetters() {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setId("2");
		playerDTO.setName("Player Two");
		playerDTO.setScore(200);
		Position position = new Position();
		position.setX(30);
		position.setY(40);

		playerDTO.setPosition(position);
		playerDTO.setDirection("down");
		playerDTO.setTankColor("blue");

		assertEquals("2", playerDTO.getId());
		assertEquals("Player Two", playerDTO.getName());
		assertEquals(200, playerDTO.getScore());
		assertNotNull(playerDTO.getPosition());
		assertEquals(30, playerDTO.getPosition().getX());
		assertEquals(40, playerDTO.getPosition().getY());
		assertEquals("down", playerDTO.getDirection());
		assertEquals("blue", playerDTO.getTankColor());
	}

	@Test
	void testPositionNullValues() {
		PlayerDTO playerDTO = new PlayerDTO();
		assertNull(playerDTO.getPosition());
	}

	@Test
	void testPlayerDTOEquality() {
		PlayerDTO playerDTO1 = new PlayerDTO();
		playerDTO1.setId("3");
		playerDTO1.setName("Player Three");
		playerDTO1.setScore(300);
		Position position1 = new Position();
		position1.setX(70);
		position1.setY(80);
		playerDTO1.setPosition(position1);
		playerDTO1.setDirection("left");
		playerDTO1.setTankColor("yellow");

		PlayerDTO playerDTO2 = new PlayerDTO();
		playerDTO2.setId("3");
		playerDTO2.setName("Player Three");
		playerDTO2.setScore(300);
		Position position2 = new Position();
		position2.setX(70);
		position2.setY(80);
		playerDTO2.setPosition(position2);
		playerDTO2.setDirection("left");
		playerDTO2.setTankColor("yellow");

		assertEquals(playerDTO1.getId(), playerDTO2.getId());
		assertEquals(playerDTO1.getName(), playerDTO2.getName());
		assertEquals(playerDTO1.getScore(), playerDTO2.getScore());
		assertEquals(playerDTO1.getPosition().getX(), playerDTO2.getPosition().getX());
		assertEquals(playerDTO1.getPosition().getY(), playerDTO2.getPosition().getY());
		assertEquals(playerDTO1.getDirection(), playerDTO2.getDirection());
		assertEquals(playerDTO1.getTankColor(), playerDTO2.getTankColor());
	}

	@Test
	void testPlayerDTONotEqual() {
		PlayerDTO playerDTO1 = new PlayerDTO();
		playerDTO1.setId("4");
		playerDTO1.setName("Player Four");
		playerDTO1.setScore(400);
		Position position1 = new Position();
		position1.setX(90);
		position1.setY(100);
		playerDTO1.setPosition(position1);
		playerDTO1.setDirection("right");
		playerDTO1.setTankColor("red");

		PlayerDTO playerDTO2 = new PlayerDTO();
		playerDTO2.setId("5");
		playerDTO2.setName("Player Five");
		playerDTO2.setScore(500);
		Position position2 = new Position();
		position2.setX(110);
		position2.setY(120);
		playerDTO2.setPosition(position2);
		playerDTO2.setDirection("up");
		playerDTO2.setTankColor("black");

		assertNotEquals(playerDTO1.getId(), playerDTO2.getId());
		assertNotEquals(playerDTO1.getName(), playerDTO2.getName());
		assertNotEquals(playerDTO1.getScore(), playerDTO2.getScore());
		assertNotEquals(playerDTO1.getPosition().getX(), playerDTO2.getPosition().getX());
		assertNotEquals(playerDTO1.getPosition().getY(), playerDTO2.getPosition().getY());
		assertNotEquals(playerDTO1.getDirection(), playerDTO2.getDirection());
		assertNotEquals(playerDTO1.getTankColor(), playerDTO2.getTankColor());
	}

	// Test Cors

}
