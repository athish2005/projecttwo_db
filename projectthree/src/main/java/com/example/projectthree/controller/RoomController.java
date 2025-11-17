package com.example.projectthree.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.projectthree.entity.Room;
import com.example.projectthree.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // ✅ Add a new room to a hotel
    @PostMapping("/hotel/{hotelId}")
    public Room addRoom(@PathVariable Long hotelId, @RequestBody Room room) {
        return roomService.addRoom(hotelId, room);
    }

    // ✅ Get rooms by hotel
    @GetMapping("/hotel/{hotelId}")
    public List<Room> getRoomsByHotel(@PathVariable Long hotelId) {
        return roomService.getRoomsByHotel(hotelId);
    }

    // ✅ Get room by ID
    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }
}
