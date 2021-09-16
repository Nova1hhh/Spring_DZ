package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petmodel = PetModel.getInstance();
    private static final AtomicInteger newID = new AtomicInteger(1);

    @PostMapping(value = "/createPet", consumes = "application/json")
    public String createPet(@RequestBody Pet pet) {
        petmodel.add(pet, newID.getAndIncrement());

        if (petmodel.getFull().size() == 1) {
            return "Вы добавили первого питомца";
        } else {
            return "Новый питомец добавлен";
        }
    }

    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer, Pet> getAll (){
        return petmodel.getFull();
    }

    /*
    {
        "id" : 123
    }
     */
    @GetMapping(value = "/getPet", produces = "application/json")
    public Pet getPet(@RequestParam int id) {
        return petmodel.getFromList(id);
    }

    @DeleteMapping(value = "/delPet", consumes = "application/json", produces = "application/json")
    public Map<Integer, Pet> delPet(@RequestBody Map<String, Integer> id) {
        petmodel.dellPet(id.get("id"));
        return petmodel.getFull();
    }

    @PutMapping(value = "/putPet", consumes = "application/json", produces = "application/json")
    public Map<Integer, Pet> putPet(@RequestBody Map<String, String> pet){
        int id = Integer.parseInt(pet.get("id"));
        String name = pet.get("name");
        String type = pet.get("type");
        int age = Integer.parseInt(pet.get("age"));
        Pet new_pet = new Pet(name, type, age);
        petmodel.add(new_pet, id);
        return petmodel.getFull();
    }

    Map<Integer, String> compass = new HashMap<>();

    public int[] parser (Map<String, String> side, String temp) {
        String str = side.get(temp);
        String[] str1 = str.split("-");
        int num1 = Integer.parseInt(str1[0]);
        int num2 = Integer.parseInt(str1[1]);
        return new int[] {num1, num2};
    }

    @PostMapping(value = "/postCompass", consumes = "application/json")
    public Map<Integer, String> createCompass(@RequestBody Map<String, String> side) {
        int[] n = parser(side, "NORTH");
        for (int i = 0; i <= n[1]; i++) {
            compass.put(i, "NORTH");
        }
        for (int i = n[0]; i <= 359; i++) {
            compass.put(i, "NORTH");
        }

        int[] ne = parser(side, "NORTHEAST");
        for (int i = ne[0]; i <= ne[1]; i++) {
            compass.put(i, "NORTHEAST");
        }

        int[] e = parser(side, "EAST");
        for (int i = e[0]; i <= e[1]; i++) {
            compass.put(i, "EAST");
        }

        int[] se = parser(side, "SOUTHEAST");
        for (int i = se[0]; i <= se[1]; i++) {
            compass.put(i, "SOUTHEAST");
        }

        int[] s = parser(side, "SOUTH");
        for (int i = s[0]; i <= s[1]; i++) {
            compass.put(i, "SOUTH");
        }

        int[] sw = parser(side, "SOUTHWEST");
        for (int i = sw[0]; i <= sw[1]; i++) {
            compass.put(i, "SOUTHWEST");
        }

        int[] w = parser(side, "WEST");
        for (int i = w[0]; i <= w[1]; i++) {
            compass.put(i, "WEST");
        }

        int[] nw = parser(side, "NORTHWEST");
        for (int i = nw[0]; i <= nw[1]; i++) {
            compass.put(i, "NORTHWEST");
        }
        return compass;
    }


    // тк запрос get, то парамерты прописываем через ?. Пример: localhost:8080/getSide?degree=6
    @GetMapping(value = "/getSide", consumes = "application/json", produces = "application/json")
    public Map<String, String> getSide(@RequestParam int degree){
        Map<String,String> ans = new HashMap<>();
        ans.put("SIDE", compass.get(degree));
        return ans;
    }
}
