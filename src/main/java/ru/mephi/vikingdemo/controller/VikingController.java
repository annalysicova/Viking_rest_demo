package ru.mephi.vikingdemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.VikingService;

import java.util.List;

@RestController
@RequestMapping("/api/vikings")
@Tag(name = "Vikings", description = "Операции с викингами")
public class VikingController {

    private final VikingService vikingService;
    private final VikingListener vikingListener;

    public VikingController(VikingService vikingService, VikingListener vikingListener) {
        this.vikingService = vikingService;
        this.vikingListener = vikingListener;
    }

    @GetMapping
    @Operation(summary = "Получить список созданных викингов", operationId = "getAllVikings")
    @ApiResponse(responseCode = "200", description = "Список успешно получен")
    public List<Viking> getAllVikings() {
        return vikingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить конкретного викинга по id", operationId = "getVikingById")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг найден"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public ResponseEntity<Viking> getVikingById(@PathVariable int id) {
        return vikingService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Добавить конкретного викинга", operationId = "createViking")
    @ApiResponse(responseCode = "201", description = "Викинг успешно создан")
    public ResponseEntity<Viking> createViking(@RequestBody Viking viking) {
        Viking created = vikingService.create(viking);
        vikingListener.showViking(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Перезаписать параметры конкретного викинга", operationId = "replaceViking")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Викинг успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public ResponseEntity<Viking> replaceViking(@PathVariable int id, @RequestBody Viking viking) {
        return vikingService.replace(id, viking)
                .map(updated -> {
                    vikingListener.showViking(updated);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить конкретного викинга из таблицы", operationId = "deleteViking")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Викинг успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Викинг не найден")
    })
    public ResponseEntity<Void> deleteViking(@PathVariable int id) {
        if (!vikingService.deleteById(id)) {
            return ResponseEntity.notFound().build();
        }
        vikingListener.removeViking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    @Operation(summary = "Получить список тестовых викингов", operationId = "getTest")
    public List<String> test() {
        return List.of("Ragnar", "Bjorn");
    }

    @PostMapping("/post")
    @Operation(summary = "Создать викинга со случайными параметрами", operationId = "postRandomViking")
    @ApiResponse(responseCode = "201", description = "Случайный викинг успешно создан")
    public ResponseEntity<Viking> addRandomViking() {
        Viking created = vikingListener.testAdd();
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
