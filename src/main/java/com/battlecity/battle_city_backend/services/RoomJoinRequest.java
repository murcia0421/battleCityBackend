// RoomJoinRequest.java
package com.battlecity.battle_city_backend.services;

public class RoomJoinRequest {
    private String name;
    private String roomId;

    public RoomJoinRequest(String name, String roomId) {
        this.name = name;
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
