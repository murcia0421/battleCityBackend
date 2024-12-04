package com.battlecity.battle_city_backend;

import com.battlecity.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleCityBackendApplicationTests {

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
}
